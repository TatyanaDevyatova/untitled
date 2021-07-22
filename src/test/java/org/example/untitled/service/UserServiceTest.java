package org.example.untitled.service;

import org.example.untitled.domain.Role;
import org.example.untitled.domain.User;
import org.example.untitled.repository.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.hamcrest.CoreMatchers;

import java.util.Collections;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSenderService mailSenderService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles())
                .matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1))
                .save(user);
        Mockito.verify(mailSenderService, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);
        Mockito.verify(userRepo, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSenderService, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );

    }


    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("bingo");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());
        Mockito.verify(userRepo, Mockito.times(1))
                .save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");

        Assert.assertFalse(isUserActivated);
        Mockito.verify(userRepo, Mockito.times(0))
                .save(ArgumentMatchers.any(User.class));

    }


}