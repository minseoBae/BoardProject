package com.example.boardproject.service;

import com.example.boardproject.domain.Board;
import com.example.boardproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> findAll(Pageable pageable) {
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findAll(sortedByDescId);
    }

    @Transactional
    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Transactional
    public void update(Board board) {
        Board existingBoard = boardRepository.findById(board.getId()).orElse(null);
        if (existingBoard != null) {
            boardRepository.save(board);
            board.setCreated_at(existingBoard.getCreated_at());
        }
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
