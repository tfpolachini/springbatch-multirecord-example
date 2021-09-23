package com.polachini.springbatchtest.batch;

import com.polachini.springbatchtest.entities.DetailEntity;
import com.polachini.springbatchtest.repository.DetailRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseReader {
    public static RepositoryItemReader reader(DetailRepository detailRepository) {
        RepositoryItemReader<DetailEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(detailRepository);
        reader.setMethodName("findByIdentificador");

        List<Object> arguments = new ArrayList<>();
        arguments.add("D");

        reader.setArguments(arguments);
        reader.setPageSize(50);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        reader.setSort(sorts);

        return reader;
    }
}
