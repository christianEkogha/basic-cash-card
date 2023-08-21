package fr.cekogha.cashcardapi.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import fr.cekogha.cashcardapi.model.CashCard;
import fr.cekogha.cashcardapi.repository.CashCardRepository;

@RestController
@RequestMapping("cashcards")
public class CashCardController {

	private CashCardRepository repository;
	
	public CashCardController(CashCardRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CashCard> findById(@PathVariable("id") Long id, Principal principal){
		CashCard cashCard = findCashCard(id, principal);
		if(cashCard != null)
			return ResponseEntity.ok(cashCard);
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCard, UriComponentsBuilder uriComponentBuilder, Principal principal) {
		CashCard savedCashCard = repository.save(new CashCard(null, newCashCard.amount(), principal.getName()));
		URI locationSavedCashCard = uriComponentBuilder
				.path("cashcards/{id}")
				.buildAndExpand(savedCashCard.id())
				.toUri();
		return ResponseEntity.created(locationSavedCashCard).build();
	}
	
	@GetMapping()
	public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal){
		Page<CashCard> page = repository.findByOwner(principal.getName(),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSortOr(Sort.by(Sort.DEFAULT_DIRECTION.ASC, "amount"))
						)
				);
		return ResponseEntity.ok(page.getContent());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateCashCard(@PathVariable Long id, @RequestBody CashCard cashCardUpdate, Principal principal){
		CashCard cashCard = findCashCard(id, principal);
		if(cashCard == null)
			return ResponseEntity.notFound().build();
		CashCard newCashCard = new CashCard(id, cashCardUpdate.amount(), principal.getName());
		repository.save(newCashCard);		
		return ResponseEntity.noContent().build();
	}
	
	private CashCard findCashCard(Long id, Principal principal) {
		return repository.findByIdAndOwner(id, principal.getName());
	}
}
