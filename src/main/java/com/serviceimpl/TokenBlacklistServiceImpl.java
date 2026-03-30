package com.serviceimpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.service.TokenBlacklistService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenBlacklistServiceImpl implements TokenBlacklistService{
	
	// Thread-safe set to store blacklisted tokens
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
	
	public void blacklistToken(String token) {

		blacklistedTokens.add(token);
        log.info("Token blacklisted: {}", token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
    	
        return blacklistedTokens.contains(token);
    }

}
