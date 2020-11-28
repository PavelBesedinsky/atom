package ru.atom.chat.repository;//package ru.atom.chat.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.atom.chat.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findAllByName(String name);
}
