package com.hhplus.server.domain.user;

import com.hhplus.server.domain.user.model.User;

public interface UserReader {
    User getUser(Long userId);
}
