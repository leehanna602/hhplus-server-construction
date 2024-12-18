package com.hhplus.server.domain.user;

import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public User getUser(Long userId) {
        return userReader.getUser(userId);
    }

    @Transactional
    public User save(User user) {
        return userWriter.save(user);
    }

}
