package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

@SpringBootApplication
public class TacoCloudApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

	@Bean
	public CommandLineRunner dataLoader(IngredientRepository repo) {

		return args -> {
			saveIfNotExists(repo, new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
			saveIfNotExists(repo, new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
			saveIfNotExists(repo, new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
			saveIfNotExists(repo, new Ingredient("CARN", "Carnitas", Type.PROTEIN));
			saveIfNotExists(repo, new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
			saveIfNotExists(repo, new Ingredient("LETC", "Lettuce", Type.VEGGIES));
			saveIfNotExists(repo, new Ingredient("CHED", "Cheddar", Type.CHEESE));
			saveIfNotExists(repo, new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
			saveIfNotExists(repo, new Ingredient("SLSA", "Salsa", Type.SAUCE));
			saveIfNotExists(repo, new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
		};

		
	}

	private void saveIfNotExists(IngredientRepository repo, Ingredient ingredient) {
		if (!repo.existsById(ingredient.getId())) {
			repo.save(ingredient);
		}
	}//IngredientRepository에 dataLoader()를 사용하여 초기 데이터를 넣기 전에 이미지 데이터가 존재하는지를 판단하는 메서드.

}
