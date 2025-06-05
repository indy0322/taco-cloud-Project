package tacos.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String>{
//<Ingredient, String> 첫번째 파라미터는 Repository에서 지속할 객체의 유형, 두번째 파라미터는 객체의 ID필드 유형이다.

    //CrudRepository는 findAll, findById, save 를 포함한 일반적인 작업을 위한 기본적인 메소드를 제공한다.
    
    //Iterable<Ingredient> findAll();

    //Optional<Ingredient> findById(String id);

    //Ingredient save(Ingredient ingredient);
}
