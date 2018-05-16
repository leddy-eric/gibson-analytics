package com.gibson.analytics.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;

@RestController
@RequestMapping("/detail")
public class GameDetailController {
	@Autowired
	MlbGameDetailRepository repository;
	
	@RequestMapping
    public MlbGameDetail getDetail(@RequestParam("id") String id) {
		Optional<MlbGameDetail> detail = repository.findByApiId(id);
		
		if(detail.isPresent()) {
			return detail.get();
		}
		
		throw new ResourceNotFoundException("No game detail exists for "+id);
    }

}
