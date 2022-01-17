package com.soldano.AlkemySpringboot.controller;

import com.soldano.AlkemySpringboot.dto.character.CharacterDto;
import com.soldano.AlkemySpringboot.dto.character.CreateCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.SimpleCharacterDto;
import com.soldano.AlkemySpringboot.dto.character.UpdateCharacterDto;
import com.soldano.AlkemySpringboot.exceptions.EntityNotFoundException;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.service.CharacterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "Characters")
@SecurityRequirement(name = "Authorization")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCharacters(@RequestParam(required = false) Map<String, String> reqParam) {
        if (reqParam.containsKey("name") || reqParam.containsKey("age") || reqParam.containsKey("movie") ) {
            List<CharacterDto> characters = characterService.getCharactersByParams(reqParam);
            if (characters.isEmpty())
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.ok(characters);
        } else {
            List<SimpleCharacterDto> characters = characterService.getAllCharacters();
            if (!characters.isEmpty())
                return ResponseEntity.ok(characters);
            else
                return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacterById(@PathVariable Integer id) {
        CharacterDto character = characterService.getCharacterById(id);

        if (character == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(character);
    }

    @PostMapping
    public ResponseEntity<CharacterDto> createCharacter(@RequestBody @Valid CreateCharacterDto character) throws UniqueException, EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.addCharacter(character));
    }

    @PutMapping("/{id}")
    ResponseEntity<CharacterDto> updateCharacter(@PathVariable Integer id, @RequestBody @Valid UpdateCharacterDto character) throws EntityNotFoundException, UniqueException {
        return ResponseEntity.ok(characterService.updateCharacter(id, character));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteCharacter(@PathVariable Integer id) throws EntityNotFoundException {
        characterService.deleteCharacter(id);
        return ResponseEntity.ok().build();
    }
}