; Peter M. Golaszewski
; CS 2135, section 4
; Homework #6 problem #1

;  Following defines all function needed for predicate Rexp? to work:

; ##############################
; # Misc Operations for Rexp?: #
; ##############################

; #t if exp is empty or it is list of numbers
; #f else
(define number_lst? (lambda (exp) (
 cond
  ((list? exp) 
   (cond
     ((null? exp) #t)
     ((number? (car exp))
	(number_lst? (cdr exp))
     ) 
     (else #f)		
   )
  ) ;else error
  (else #f)
)))

; #t if exp is non-empty or it is list of numbers
; #f else
(define tuple? (lambda (exp) (
  if (number_lst? exp)
  (
	if (null? exp) #f #t
  ) #f
)))

; counts top level numbers of elements
; if it is not a list it returns 0
(define count (lambda (lst) (
  cond
  ((not (list? lst))  0)
  ((null? lst) 0)
  (else (+ 1 (count (cdr lst))))  
)))

; Checks if it is a r expresion
(define r? (lambda (exp) (
 cond 
  ((not (list? exp)) #f)
  ((null? exp) #t)
  ((tuple? (car exp)) (r? (cdr exp)))
  (else #f)
)))

; Checks if exp is a cond expresion
(define cond? (lambda (exp) (
 cond
  ((not (list? exp)) #f)
  ((and (eq? (count exp) 3) (number? (car exp)) (number? (caddr exp))) #t
)  
  (else #f)
)))

; Checks if exp is a binrep expression
(define binrop? (lambda (exp) (
    case exp
    ((U D CP) #t)
    (else #f)
)))

; Checks if exp is a P expression
(define P? (lambda (exp) (
 cond
  ((not (list? exp)) #f)
  ((not (eq? 3 (count exp))) #f)
  (
   (and (eq? 'P (car exp))
        (tuple? (cadr exp))
        (rexp? (caddr exp))
   ) #t
  )
  (else #f)
)))

; Checks if exp is a S expression
(define S? (lambda (exp) (
 cond
  ((not (list? exp)) #f)
  ((not (eq? 3 (count exp))) #f)
  (
   (and (eq? 'S (car exp))
        (cond? (cadr exp))
        (rexp? (caddr exp))
   ) #t
  )
  (else #f)
)))


; Checks if exp is a binrop with 2 repx's 
(define binropRR? (lambda (exp) (
 cond
  ((not (list? exp)) #f)
  ((not (eq? 3 (count exp))) #f)
  (
   (and (binrop? (car exp))
        (rexp? (cadr exp))
        (rexp? (caddr exp))
   ) #t
  )
  (else #f)
)))

; ####################
; # Predicate Rexp?: #
; ####################

(define REXP? (lambda (exp) (
 cond
  ((p? exp) #t)
  ((s? exp) #t)
  ((binropRR? exp) #t)
  ((r? exp) #t)
  (else #f)
)))



