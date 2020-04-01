package com.example.connector.axon;

import com.example.connector.axon.event.UserAddedEvent;
import com.example.connector.axon.event.UserDeletedEvent;
import com.example.connector.axon.event.UserUpdatedEvent;
import com.example.connector.axon.query.FindUsersQuery;
import com.example.connector.domain.User;
import com.example.connector.repository.UserRepositoryImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class UserProjector {

    private UserRepositoryImpl userRepository;

    public UserProjector(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @EventHandler
    public void handle(UserAddedEvent userAddedEvent) throws InvocationTargetException, IllegalAccessException {
        User user = new User();
        BeanUtils.copyProperties(user, userAddedEvent);
        userRepository.save(user);
    }

    @EventHandler
    public void handle(UserDeletedEvent userDeletedEvent) throws ExecutionException, InterruptedException {
        userRepository.delete(userDeletedEvent.getId());
    }

    @EventHandler
    public void handle(UserUpdatedEvent userUpdatedEvent) throws InvocationTargetException, IllegalAccessException, ExecutionException, InterruptedException {
        User user = new User();
        BeanUtils.copyProperties(user, userUpdatedEvent);
        userRepository.update(user);
    }

    @QueryHandler
    public List<User> getUsers(FindUsersQuery query) {
        return userRepository.findAll(query.getQueryParams()).block();
    }
}