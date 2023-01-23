package BikeRentalSystem.config;

import BikeRentalSystem.models.RentalBike;
import BikeRentalSystem.repositories.BikeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step bikeStep) {
        return jobBuilderFactory.get("bike-backup-job")
                .incrementer(new RunIdIncrementer())
                .start(bikeStep)
                .build();
    }

    @Bean
    public Step bikeStep(StepBuilderFactory stepBuilderFactory, ItemReader<RentalBike> csvReader, BikeProcessor processor, BikeWriter writer) {
        // This step just reads the csv file and then writes the entries into the database
        return stepBuilderFactory.get("bike-step")
                .<RentalBike, RentalBike>chunk(100)
                .reader(csvReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    public FlatFileItemReader<RentalBike> csvReader(@Value("${inputFile}") String inputFile) {
        return new FlatFileItemReaderBuilder<RentalBike>()
                .name("csv-reader")
                .resource(new ClassPathResource(inputFile))
                .delimited()
                .names("id", "brand", "color", "model")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{setTargetType(RentalBike.class);}})
                .build();
    }

    @Bean
    public RepositoryItemReader<RentalBike> repositoryReader(BikeRepository bikeRepository) {
        return new RepositoryItemReaderBuilder<RentalBike>()
                .repository(bikeRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .name("repository-reader")
                .build();
    }

    @Component
    public static class BikeProcessor implements ItemProcessor<RentalBike, RentalBike> {
        // This helps you to process the names of the employee at a set time
        @Override
        public RentalBike process(RentalBike rentalBike) {
            rentalBike.setBrand(rentalBike.getBrand().toUpperCase());
            rentalBike.setColor(rentalBike.getColor().toUpperCase());
            rentalBike.setModel(rentalBike.getModel().toUpperCase());
            return rentalBike;
        }
    }

    @Component
    public static class BikeWriter implements ItemWriter<RentalBike> {

        @Autowired
        private BikeRepository bikeRepository;

        @Value("${sleepTime}")
        private Integer SLEEP_TIME;

        @Override
        public void write(List<? extends RentalBike> rentalBikes) throws InterruptedException {
            bikeRepository.saveAll(rentalBikes);
            Thread.sleep(SLEEP_TIME);
            System.out.println("Saved bikes: " + rentalBikes);
        }
    }

}
