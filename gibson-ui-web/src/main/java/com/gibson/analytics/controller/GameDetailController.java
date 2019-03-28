package com.gibson.analytics.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.core.baseball.MlbApiMonitor;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;

@RestController
@RequestMapping("/detail")
public class GameDetailController {
	@Autowired
	MlbGameDetailRepository repository;
	
	@Autowired
	MlbApiMonitor monitor;
	
	@RequestMapping
    public MlbGameDetail getDetail(@RequestParam("id") String id) {
		Optional<MlbGameDetail> detail = repository.findByApiId(id);
		
		if(detail.isPresent()) {
			return detail.get();
		}
		
		throw new ResourceNotFoundException("No game detail exists for "+id);
    }
	
	 @RequestMapping(value = "/all", method = RequestMethod.GET)
	 public List<MlbGameDetail> findAll() {
		 return repository.findAll(); 
	 }
	 
	 @RequestMapping(value = "/refresh/{id}", method = RequestMethod.POST)
	 public MlbGameDetail findAll(@PathVariable long id) {
		 MlbGameDetail gameDetail = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
		 
		 // Update It
		 gameDetail.setStatus(MlbGameStatus.REFRESH);
		 
		 return repository.save(gameDetail);
	 }
	 
	 @RequestMapping(value = "/refresh/force/{date}", method = RequestMethod.POST)
	 public HttpStatus findAll(@PathVariable String date) {
		 monitor.refreshGameData(LocalDate.parse(date));
		 
		 return HttpStatus.OK;
	 }

}
