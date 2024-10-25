package com.hhplus.server.domain.user;

import com.hhplus.server.domain.user.model.User;

public interface UserWriter {
    User save(User user);
}
