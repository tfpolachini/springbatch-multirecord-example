package com.polachini.springbatchtest.configuration;

import com.polachini.springbatchtest.batch.*;
import com.polachini.springbatchtest.entities.DetailEntity;
import com.polachini.springbatchtest.models.BaseModel;
import com.polachini.springbatchtest.models.Detail;
import com.polachini.springbatchtest.models.Header;
import com.polachini.springbatchtest.models.Trailer;
import com.polachini.springbatchtest.repository.DetailRepository;
import com.polachini.springbatchtest.repository.HeaderRepository;
import com.polachini.springbatchtest.repository.TrailerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.SubclassClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class Batch {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private HeaderRepository headerRepository;

  @Autowired
  private DetailRepository detailRepository;

  @Autowired
  private TrailerRepository trailerRepository;

  @Bean
  public Job job() {
    return jobBuilderFactory
        .get("job")
        .incrementer(new RunIdIncrementer())
        .listener(new Listener())
            .start(step1())
            .next(step2())
            .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory
        .get("step1")
        .<Header, Header>chunk(1)
        .reader(FileReader.reader("data.csv"))
        .processor(fileProcessor())
        .writer(databaseWriter())
        .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory
            .get("step2")
            .<Detail, Detail>chunk(1)
            .reader(DatabaseReader.reader(detailRepository))
            .processor(databaseProcessor())
            .writer(fileWriter())
            .build();

  }

  private ItemWriter fileWriter() {
    FlatFileItemWriter<DetailEntity> writer = new FlatFileItemWriter<>();
    writer.setResource(new FileSystemResource("resources/output.csv"));
    writer.setAppendAllowed(true);
    writer.setLineAggregator(new DelimitedLineAggregator<DetailEntity>() {
      {
        setDelimiter(";");
        setFieldExtractor(new BeanWrapperFieldExtractor<DetailEntity>() {
          {
            setNames(new String[] { "identificador", "nome", "idade" });
          }
        });
      }
    });

    return writer;
  }

  @Bean
  @StepScope
  public ItemProcessor<DetailEntity, DetailEntity> databaseProcessor() {

    return detail -> {
      System.out.println(String.format("%s, %s, %s", detail.getIdentificador(), detail.getNome(), detail.getIdade()));
      return detail;
    };
  }

  @Bean
  @StepScope
  public ItemProcessor fileProcessor() {

    ClassifierCompositeItemProcessor processor = new ClassifierCompositeItemProcessor();
    SubclassClassifier<BaseModel, ItemProcessor> classifier = new SubclassClassifier<>();
    classifier.add(Header.class, new HeaderProcessor());
    classifier.add(Detail.class, new DetailProcessor());
    classifier.add(Trailer.class, new TrailerProcessor());
    processor.setClassifier(classifier);

    return processor;
  }

  @Bean
  @StepScope
  public ItemWriter databaseWriter() {

    ClassifierCompositeItemWriter writer = new ClassifierCompositeItemWriter();
    SubclassClassifier<BaseModel, ItemWriter> classifier = new SubclassClassifier<>();
    classifier.add(Header.class, new HeaderWriter(headerRepository));
    classifier.add(Detail.class, new DetailWriter(detailRepository));
    classifier.add(Trailer.class, new TrailerWriter(trailerRepository));
    writer.setClassifier(classifier);

    return writer;
  }
}
