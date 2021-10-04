package com.lunz.springit.service;

import com.lunz.springit.domain.Link;
import com.lunz.springit.domain.Vote;
import com.lunz.springit.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }
}
