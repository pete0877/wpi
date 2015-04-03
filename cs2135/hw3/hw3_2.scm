; Peter M. Golaszewski
; CS 2135, section 4
; Homework #3 problem #2

; ------- Here are some help functions : -------------

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

;=-------- HERE ARE ADT FF Functions: --------

(define create-empty-ff (lambda () '()))

(define extend-ff* (lambda (slst vlst ff) (
	cons (cons slst (list->vector vlst)) ff
)))

; I wasn't sure which name you preffer, so I declared both of them:

(define extend-ff (lambda (slst vlst ff) (
	cons (cons slst (list->vector vlst)) ff
)))

(define apply-ff (lambda (ff s) (
	if (null? ff) (error "Apply-FF: Invalid finite function!")
	; ELSE
	(
	  let ((result (ribassoc s (caar ff) (cdar ff) 'Not_Found)))
	  (if (eq? result 'Not_Found) (apply-ff (cdr ff) s) 
	  ; Else:
	  result
	  )
	)
)))

