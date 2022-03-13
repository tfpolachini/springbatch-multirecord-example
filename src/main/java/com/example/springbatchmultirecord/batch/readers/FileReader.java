package com.example.springbatchmultirecord.batch.readers;

import com.example.springbatchmultirecord.models.BaseModel;
import com.example.springbatchmultirecord.models.Detail;
import com.example.springbatchmultirecord.models.Header;
import com.example.springbatchmultirecord.models.Trailer;

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
  public static FlatFileItemReader<BaseModel> reader(String path) {

    FlatFileItemReader<BaseModel> reader = new FlatFileItemReader<>();

    reader.setResource(new ClassPathResource(path));
    reader.setLineMapper(createRemessaLineMapper());

    return reader;
  }

  private static PatternMatchingCompositeLineMapper<BaseModel> createRemessaLineMapper() {
    var lineMapper = new PatternMatchingCompositeLineMapper<BaseModel>();

    Map<String, LineTokenizer> tokenizers = new HashMap<>(3);
    tokenizers.put("H*", headerTokenizer());
    tokenizers.put("D*", detailTokenizer());
    tokenizers.put("T*", trailerTokenizer());

    lineMapper.setTokenizers(tokenizers);

    Map<String, FieldSetMapper<BaseModel>> mappers = new HashMap<>(3);
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

  private static FieldSetMapper<BaseModel> headerFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<BaseModel>();

    fieldSetMapper.setTargetType(Header.class);

    return fieldSetMapper;
  }

  private static FieldSetMapper<BaseModel> detailFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<BaseModel>();

    fieldSetMapper.setTargetType(Detail.class);

    return fieldSetMapper;
  }

  private static FieldSetMapper<BaseModel> trailerFieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<BaseModel>();

    fieldSetMapper.setTargetType(Trailer.class);

    return fieldSetMapper;
  }
}
