package nl.fontys.s3.grp1.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.grp1.business.JournalistService;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistResponse;
import nl.fontys.s3.grp1.domain.Journalist;
import nl.fontys.s3.grp1.domain.dto.journalist.UpdateJournalistRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/journalists")
@AllArgsConstructor
public class JournalistController {
    private final JournalistService journalistService;
    @PostMapping()
    public ResponseEntity<Object> createJournalist(@RequestBody @Valid CreateJournalistRequest request) {
        try{
            CreateJournalistResponse response = journalistService.createJournalist(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Journalist> getJournalist(@PathVariable(value = "id") final long id){
        final Optional<Journalist> journalistOptional = journalistService.getJournalist(id);
        if (journalistOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(journalistOptional.get());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteJournalist(@PathVariable int id) {
        journalistService.deleteJournalist(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateJournalist(@PathVariable("id") long id,
                                                   @RequestBody @Valid UpdateJournalistRequest request){
        request.setId(id);
        try{
            journalistService.updateJournalist(request);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
