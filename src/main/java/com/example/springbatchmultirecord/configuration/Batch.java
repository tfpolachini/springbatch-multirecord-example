package com.example.springbatchmultirecord.configuration;

import com.example.springbatchmultirecord.batch.*;
import com.example.springbatchmultirecord.batch.processors.DetailProcessor;
import com.example.springbatchmultirecord.batch.processors.HeaderProcessor;
import com.example.springbatchmultirecord.batch.processors.TrailerProcessor;
import com.example.springbatchmultirecord.batch.readers.DatabaseReader;
import com.example.springbatchmultirecord.batch.readers.FileReader;
import com.example.springbatchmultirecord.batch.writers.DetailWriter;
import com.example.springbatchmultirecord.batch.writers.HeaderWriter;
import com.example.springbatchmultirecord.batch.writers.TrailerWriter;
import com.example.springbatchmultirecord.entities.DetailEntity;
import com.example.springbatchmultirecord.models.BaseModel;
import com.example.springbatchmultirecord.models.Detail;
import com.example.springbatchmultirecord.repository.DetailRepository;
import com.example.springbatchmultirecord.repository.HeaderRepository;
import com.example.springbatchmultirecord.repository.TrailerRepository;
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
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

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
        .<BaseModel, BaseModel>chunk(1)
        .reader(FileReader.reader("data.csv"))
        .processor(classifierCompositeItemProcessor())
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
      System.out.printf("%s, %s, %s%n", detail.getIdentificador(), detail.getNome(), detail.getIdade());
      return detail;
    };
  }

  @Bean
  @StepScope
  public ClassifierCompositeItemProcessor<BaseModel, BaseModel> classifierCompositeItemProcessor() {
    ClassifierCompositeItemProcessor<BaseModel, BaseModel> itemProcessor = new ClassifierCompositeItemProcessor<>();
    itemProcessor.setClassifier(new Classifier<BaseModel, ItemProcessor<?, ? extends BaseModel>>() {
      @Override
      public ItemProcessor<?, ? extends BaseModel> classify(BaseModel classifiable) {
        if (classifiable.getIdentificador().equals("H")) {
          return new HeaderProcessor();
        } else if (classifiable.getIdentificador().equals("D")) {
          return new DetailProcessor();
        } else {
          return new TrailerProcessor();
        }
      }
    });
    return itemProcessor;
  }

  @Bean
  @StepScope
  public ItemWriter<BaseModel> databaseWriter() {

    ClassifierCompositeItemWriter<BaseModel> writer = new ClassifierCompositeItemWriter<>();
    writer.setClassifier(new Classifier<BaseModel, ItemWriter<? super BaseModel>>() {
      @Override
      public ItemWriter classify(BaseModel classifiable) {
        if (classifiable.getIdentificador().equals("H")) {
          return new HeaderWriter(headerRepository);
        } else if (classifiable.getIdentificador().equals("D")) {
          return new DetailWriter(detailRepository);
        } else {
          return new TrailerWriter(trailerRepository);
        }
      }
    });
    return writer;
  }
}