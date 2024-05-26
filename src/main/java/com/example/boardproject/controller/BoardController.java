package com.example.boardproject.controller;

import com.example.boardproject.domain.Board;
import com.example.boardproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public String board(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boardList = boardService.findAll(pageable);
        model.addAttribute("boardList", boardList);
        model.addAttribute("currentPage", page);
        return "board/list";
    }

    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        Board board = boardService.findById(id);
        if(board != null) {
            model.addAttribute("board", board);
            return "board/detail";
        } else
            return "redirect:/board";
    }

    @GetMapping("/writeForm")
    public String add(Model model) {
        model.addAttribute("board", new Board());
        return "board/writeForm";
    }

    @PostMapping("/write")
    public String add(@ModelAttribute Board board) {
        boardService.save(board);
        return "redirect:/board";
    }

    @GetMapping("/updateForm/{id}")
    public String update(@PathVariable Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Board board) {
        boardService.update(board);
        return "redirect:/board";
    }

    @GetMapping("deleteForm/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);

        return "board/deleteForm";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam String password, Model model) {
        Board board = boardService.findById(id);

        if (board != null && board.getPassword().equals(password)) {
            boardService.delete(id);
            return "redirect:/board";
        } else {
            model.addAttribute("board", board);
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "board/deleteForm";
        }
    }
}
