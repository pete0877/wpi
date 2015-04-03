; Peter M. Golaszewski
; CS 2135, section 4
; Homework #6 problem #4

; This is simple REXP interpreter.
; In order to evaluate a recp expresion you need to type:
; (eval-rexp (rexp-parser rexp-expr ))
; This code also includes definitions from prob #3

(load "/cs/cs2135/mitmacros.scm")

; ###########################
; # Basic Rexp definitions: #
; ###########################

(define-record prec (nths rexp))
(define-record srec (cnd rexp))
(define-record condit (cnd))
(define-record brop (oper rexp1 rexp2))
(define-record rrec (tuples))

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

; Checks if exp is a <conjcond) expresion
(define conjcond? (lambda (exp) (
 cond 
  ((null? exp) #t)
  ((and 
	(eq? (count (car exp)) 3) 
	(number? (caar exp)) 
	(number? (caddar exp))
   ) (conjcond? (cdr exp)) 
  ) 
  (else #f)
)))

; Checks if exp is a <cond> expresion
(define cond? (lambda (exp) (
 cond
  ((not (list? exp)) #f)
  ((eq? 'and (car exp))	
	(and
		(not (null? (cdr exp)))
		(conjcond? (cdr exp))
	)
  )  
  ((and (eq? (count exp) 3) (number? (car exp)) (number? (caddr exp))) #t
  )  
  (else #f)
)))

; Checks if exp is a binrep expression
(define binrop? (lambda (exp) (
    case exp
    ((U D CP IS) #t)
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

; ###############################
; # Basic Interpreter Funcions: #
; ###############################

; This is my help function that generates list of lists
; such : ((t+e1) (t+e2) ... (t+en)) where t is a list
; and e1 .. en are other lists inside list s.
(define maketerm (lambda (t s) (
	cond 	
	((null? s) '())
	(else (cons (append t (car s)) (maketerm t (cdr s)))  
        )
)))


(define tuple-eq? (lambda (t1 t2) (
 cond
  ( (and (null? t1) (null? t2)) #t )
  ( (or (null? t1) (null? t2)) #f )
  ( (eq? (car t1) (car t2)) (tuple-eq? (cdr t1) (cdr t2)) )
  (else #f)
)))

;contains? of a {b,a,c} is true, contains? of a { } is false, 
;contains? of a {b,a} is true 
(define contains? (lambda (elt s) (
  cond
	((null? s) #f)
 	((tuple-eq? elt (car s)) #t)
	(else (contains? elt (cdr s)))
)))

; intersect of {c,a,b} {d,b,e} is {b}, 
; intersect of {c,a,b} {d,f,e} is { } 
(define intersect (lambda (s1 s2) (
  cond
	((null? s1) '())
	((contains? (car s1) s2) 
	   (cons (car s1) (intersect (cdr s1) s2))
	)
	(else (intersect (cdr s1) s2))
)))

; D of {a,b,c} {e,c,d} is {a,b}, 
; D of {b,c} { } is {b,c}, D of { } {a} is { } 
(define Differ (lambda (s1 s2) (
  cond
	((null? s1) '())
	((contains? (car s1) s2) 
	   (Differ (cdr s1) s2)		   
	)
	(else (cons (car s1) (Differ (cdr s1) s2)))
)))

; union of r1 and r2
(define Union (lambda (r1 r2) (
 append r1 (Differ r2 (intersect r1 r2))
)))

; CP of { } {a,b} is { }, 
; CP of {a,b} {c,d,a,b} is 
; {(a+c),(a+d),(a+a),(a+b),(b+c),(b+d),(b+a),(b+b)} 
(define CarProd (lambda (s1 s2) (
	cond 	
	((null? s1) '())
	(else (append (maketerm (car s1) s2) (carprod (cdr s1) s2)))
)))

; intersect of {c,a,b} {d,b,e} is {b}, 
; intersect of {c,a,b} {d,f,e} is { } 
(define intersect (lambda (s1 s2) (
  cond
	((null? s1) '())
	((contains? (car s1) s2) 
	   (cons (car s1) (intersect (cdr s1) s2))
	)
	(else (intersect (cdr s1) s2))
))) 

(define eval-op (lambda (op arg1 arg2) (
 cond
 ((eq? op '=) (if (eq? arg1 arg2) #t #f))
 ((eq? op '>) (if (> arg1 arg2) #t #f))
 ((eq? op '<) (if (< arg1 arg2) #t #f))
 ((eq? op 'U) (Union arg1 arg2))
 ((eq? op 'D) (Differ arg1 arg2))
 ((eq? op 'CP) (CarProd arg1 arg2))
 ((eq? op 'IS) (Intersect arg1 arg2))
 (else (error "Error: Invalid syntax: " op))
)))


; selects using one <singlecond>
(define select_one (lambda (cnd r) (
 cond
  ((null? r) '())
  ( else 
   (
    if  ( 
	    eval-op  	    	
                (cadr cnd) 
		(list-ref (car r) (car cnd)) 
		(caddr cnd) 
        )  
    (cons (car r) (select_one cnd (cdr r)))  
    (select_one cnd (cdr r))
   )
  )
)))

; selects from r using all <cond> in cnds
(define select_all (lambda (cnds r) (
 cond
  ((null? cnds) r)
  (else
    (Intersect (select_one (car cnds) r) 
	       (select_all (cdr cnds) r) 
    )
  )
)))

; selects from r using cnds condition(s)
(define select (lambda (cnds r) (
 cond
  ( (eq? 'and (car cnds)) (select_all (cdr cnds) r))
  ( else (select_one cnds r) )
)))

(define make-proj-term (lambda (indx lst) (
 cond 
   ((or (null? lst) (null? indx)) '())
   (else
	(cons (list-ref lst (car indx)) (make-proj-term (cdr indx) lst))
   )
)))

(define project (lambda (indx tpl) (
cond  
   ((or (null? tpl) (null? indx)) '())
   (else
	(cons (make-proj-term indx (car tpl)) (project indx (cdr tpl)))
   )
)))


(define rexp-parser (lambda (exp) (
 cond
  ((not (rexp? exp))
	(error "Error: Invalid syntax: " exp)
  )
  ((r? exp) (make-rrec exp))
  ((binrop? (car exp)) (make-brop (car exp) 
			          (rexp-parser (cadr exp))
			          (rexp-parser (caddr exp))
		       )
  )
  (else 
      (case (car exp)
        ((P) (make-prec (cadr exp) 
			(rexp-parser (caddr exp))
	     )
        )
	((S) (make-srec (make-condit (list (caadr exp) 
				           (cadadr exp) 
				           (car (cddadr exp))
 				     )
			)
			(rexp-parser (caddr exp)) 
             )
        )
	(else (error "Error: Invalid syntax: " exp))
      )
  )
)))

(define eval-rexp (lambda (exp) (
 variant-case exp
  (rrec (tuples) tuples)
  (condit (cnd) cnd)
  (prec (nths rexp) (project nths (eval-rexp rexp)))
  (srec (cnd rexp) (select (eval-rexp cnd) (eval-rexp rexp)))
  (brop (oper rexp1 rexp2)
        (eval-op oper (eval-rexp rexp1) (eval-rexp rexp2))
  )
  (else (error "Error: Invalid syntax: " exp))
)))


