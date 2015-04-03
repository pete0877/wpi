; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #1
;    This Function returns the value in v that is associated with s, of
; fail-value if there is no associated value. If the first occurence of s
; in los has index n, then the associated with s is the n-th value in v

(define RibAssoc (lambda (s los v fail-value) (
  if (null? los) fail-value
  ; ELSE
  (
	if (eq? (car los) s) 
	   (vector-ref v (- (vector-length v) (length los)))
	;ELSE
           (RibAssoc s (cdr los) v fail-value)
  )
)))

