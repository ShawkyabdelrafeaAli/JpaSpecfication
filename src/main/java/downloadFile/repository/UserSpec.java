package downloadFile.repository;

import downloadFile.entity.Category;
import downloadFile.entity.User;
import downloadFile.entity.UserSearch;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpec implements Specification<User> {

    private UserSearch Search;

    public UserSpec(UserSearch search) {
      this.Search = search;
    }
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate>predicates = new ArrayList<>();

        if(Search.getFirstName() !=null &&! Search.getFirstName().isEmpty()){
            predicates.add(cb.like(root.get("firstName"),Search.getFirstName()));
        }
        if(Search.getEmail() !=null &&! Search.getEmail().isEmpty()){
            predicates.add(cb.like(root.get("email"),Search.getEmail()));
        }
        if(Search.getLastName() !=null &&! Search.getLastName().isEmpty()){
            predicates.add(cb.equal(root.get("lastname"),Search.getLastName()));
        }
        return cb.and(predicates.toArray(new  Predicate[0] ));
    }
//    public static Specification<User> writtenByCategory(String categoryName) {
//        return (root, query, criteriaBuilder) -> {
//            Join<User, Category> categoryJoin = root.join("category");
//            return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
//        };
//    }
    public static Specification<User> hasAuthor(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            Join<User, Category> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
        };
    }

}
