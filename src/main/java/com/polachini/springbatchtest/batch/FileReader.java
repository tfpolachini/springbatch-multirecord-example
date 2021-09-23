package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.models.Detail;
import com.polachini.springbatchtest.models.Header;
import com.polachini.springbatchtest.models.Trailer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.core.io.ClassPathResource;

public class FileReader {
  public static FlatFileItemReader reader(String path) {

    FlatFileItemReader reader = new FlatFileItemReader();

    reader.setResource(new ClassPathResource(path));
    reader.setLineMapper(createRemessaLineMapper());

    return reader;
  }

  private static PatternMatchingCompositeLineMapper createRemessaLineMapper() {
    var lineMapper = new PatternMatchingCompositeLineMapper();

    Map<String, LineTokenizer> tokenizers = new HashMap<>(3);
    tokenizers.put("H*", headerTokenizer());
    tokenizers.put("D*", detailTokenizer());
    tokenizers.put("T*", trailerTokenizer());

    lineMapper.setTokenizers(tokenizers);

    Map<String, FieldSetMapper> mappers = new HashMap<>(3);
    mappers.put("H*", headerFieldSetMapper());
    mappers.put("D*", detailFieldSetMapper());
    mappers.put("T*", trailerFieldSetMapper());

    lineMapper.setFieldSetMappers(mappers);

    return lineMapper;
  }

  private static LineTokenizer headerTokenizer() {
    var lineTokenizer = new DelimitedLineTokenizer();

    lineTokenizer.setNames("identificador", "banco", "agencia");
    lineTokenizer.setDelimiter(";");

    return lineTokenizer;
  }

  private static LineTokenizer detailTokenizer() {
    var lineTokenizer = new DelimitedLineTokenizer();

    lineTokenizer.setNames("identificador", "nome", "idade");
    lineTokenizer.setDelimiter(";");

    return lineTokenizer;
  }

  private static LineTokenizer trailerTokenizer() {
    var lineTokenizer = new DelimitedLineTokenizer();

    lineTokenizer.setNames("identificador", "clientes");
    lineTokenizer.setDelimiter(";");

    return lineTokenizer;
  }

  private static FieldSetMapper<Header> headerFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<Header>();

    fieldSetMapper.setTargetType(Header.class);

    return fieldSetMapper;
  }

  private static FieldSetMapper<Detail> detailFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<Detail>();

    fieldSetMapper.setTargetType(Detail.class);

    return fieldSetMapper;
  }

  private static FieldSetMapper<Trailer> trailerFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<Trailer>();

    fieldSetMapper.setTargetType(Trailer.class);

    return fieldSetMapper;
  }
}
