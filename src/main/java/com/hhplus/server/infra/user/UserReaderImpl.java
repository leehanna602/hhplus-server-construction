package com.hhplus.server.infra.user;

import com.hhplus.server.domain.user.UserReader;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUser(Long userId) {
        return userJpaRepository.findByUserId(userId);
    }
}
