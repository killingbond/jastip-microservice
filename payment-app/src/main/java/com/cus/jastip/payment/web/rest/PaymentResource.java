package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.enumeration.UpdateType;
import com.cus.jastip.payment.repository.PaymentRepository;
import com.cus.jastip.payment.repository.search.PaymentSearchRepository;
import com.cus.jastip.payment.service.PaymentAuditService;
import com.cus.jastip.payment.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.payment.web.rest.util.HeaderUtil;
import com.cus.jastip.payment.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Payment.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {
	
	@Autowired
	private PaymentAuditService paymentAuditService;

	private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

	private static final String ENTITY_NAME = "payment";

	private final PaymentRepository paymentRepository;

	private final PaymentSearchRepository paymentSearchRepository;

	public PaymentResource(PaymentRepository paymentRepository, PaymentSearchRepository paymentSearchRepository) {
		this.paymentRepository = paymentRepository;
		this.paymentSearchRepository = paymentSearchRepository;
	}

	/**
	 * POST /payments : Create a new payment.
	 *
	 * @param payment
	 *            the payment to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         payment, or with status 400 (Bad Request) if the payment has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/payments")
	@Timed
	public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
		log.debug("REST request to save Payment : {}", payment);
		if (payment.getId() != null) {
			throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Payment result = paymentRepository.save(payment);
		paymentSearchRepository.save(result);
		paymentAuditService.addPayment(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /payments : Updates an existing payment.
	 *
	 * @param payment
	 *            the payment to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         payment, or with status 400 (Bad Request) if the payment is not
	 *         valid, or with status 500 (Internal Server Error) if the payment
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/payments")
	@Timed
	public ResponseEntity<Payment> updatePayment(@Valid @RequestBody Payment payment) throws URISyntaxException {
		log.debug("REST request to update Payment : {}", payment);
		if (payment.getId() == null) {
			return createPayment(payment);
		}

		Payment result = paymentRepository.save(payment);
		paymentSearchRepository.save(result);
		paymentAuditService.addPayment(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payment.getId().toString()))
				.body(result);
	}

	/**
	 * GET /payments : get all the payments.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of payments in
	 *         body
	 */
	@GetMapping("/payments")
	@Timed
	public ResponseEntity<List<Payment>> getAllPayments(Pageable pageable) {
		log.debug("REST request to get all Payments");
		Page<Payment> page = paymentRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /payments/:id : get the "id" payment.
	 *
	 * @param id
	 *            the id of the payment to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the payment, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/payments/{id}")
	@Timed
	public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
		log.debug("REST request to get Payment : {}", id);
		Payment payment = paymentRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payment));
	}

	/**
	 * DELETE /payments/:id : delete the "id" payment.
	 *
	 * @param id
	 *            the id of the payment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/payments/{id}")
	@Timed
	public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
		log.debug("REST request to delete Payment : {}", id);
		Payment result = paymentRepository.findOne(id);
		paymentRepository.delete(id);
		paymentSearchRepository.delete(id);
		paymentAuditService.addPayment(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/payments?query=:query : search for the payment corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the payment search
	 * @return the result of the search
	 */
	@GetMapping("/_search/payments")
	@Timed
	public ResponseEntity<List<Payment>> searchPayments(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Payments for query {}", query);
		List<Payment> payment = StreamSupport
				.stream(paymentSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Payment> page = new PageImpl<>(payment);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/payments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}
