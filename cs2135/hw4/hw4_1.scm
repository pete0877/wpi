; Peter M. Golaszewski
; CS 2135, section 4
; Homework #4 problem #1     

;  This is predicate of LEXP type it returns #t if the expression E is
; a lexp type, or returns '() if it is not

(define LEXP? (Lambda (e)(
  cond
  ((symbol? e) #t)
  ; Check all possible cases and the lexp?s of the operators
  ((eq? (car e) 'land) (and (LEXP? (cadr e)) (LEXP? (caddr e))))
  ((eq? (car e) 'lor) (and (LEXP? (cadr e)) (LEXP? (caddr e))))
  ((eq? (car e) 'limplies) (and (LEXP? (cadr e)) (LEXP? (caddr e))))
  ((eq? (car e) 'lequivalent) (and (LEXP? (cadr e)) (LEXP? (caddr e))))
  ((eq? (car e) 'lnot) (LEXP? (cadr e)) )
  ; not a lexp expression:
  (else #f)
)))  
