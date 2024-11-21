package com.hhplus.server.infra.db.user;

import com.hhplus.server.domain.user.UserWriter;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UserWriterImpl implements UserWriter {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

}
