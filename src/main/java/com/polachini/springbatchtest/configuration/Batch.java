package com.polachini.springbatchtest.configuration;

import com.polachini.springbatchtest.batch.DetailProcessor;
import com.polachini.springbatchtest.batch.DetailWriter;
import com.polachini.springbatchtest.batch.HeaderProcessor;
import com.polachini.springbatchtest.batch.HeaderWriter;
import com.polachini.springbatchtest.batch.Listener;
import com.polachini.springbatchtest.batch.Reader;
import com.polachini.springbatchtest.batch.TrailerProcessor;
import com.polachini.springbatchtest.batch.TrailerWriter;
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
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.SubclassClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        .flow(step1())
        .end()
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory
        .get("step1")
        .<Header, Header>chunk(1)
        .reader(Reader.reader("data.csv"))
        .processor(processor())
        .writer(writer())
        .build();
  }

  @Bean
  @StepScope
  public ItemProcessor processor() {

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
  public ItemWriter writer() {

    ClassifierCompositeItemWriter writer = new ClassifierCompositeItemWriter();
    SubclassClassifier<BaseModel, ItemWriter> classifier = new SubclassClassifier<>();
    classifier.add(Header.class, new HeaderWriter(headerRepository));
    classifier.add(Detail.class, new DetailWriter(detailRepository));
    classifier.add(Trailer.class, new TrailerWriter(trailerRepository));
    writer.setClassifier(classifier);

    return writer;
  }
}
