package talk.demo;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class BatchFileMapper {
	
	@Bean
	public FlatFileItemReader<ClientInfo> reader(S3ObjectInputStream objectContent) {
		FlatFileItemReader<ClientInfo> reader = new FlatFileItemReader<>();
		reader.setResource(new InputStreamResource(objectContent));
		reader.setLinesToSkip(1);
		reader.setSkippedLinesCallback(System.out::println);
		
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames(new String[] {
				"document",
				"name",
				"age"
		});
		
		BeanWrapperFieldSetMapper<ClientInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(ClientInfo.class);
		
		DefaultLineMapper<ClientInfo> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		reader.setLineMapper(lineMapper);
		
		return reader;
	}

}
