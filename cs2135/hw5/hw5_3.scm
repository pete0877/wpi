; Peter M. Golaszewski
; CS 2135, section 4
; Homework #5 problem #3

;  This is modificated version of the interpreter,
; which allows to use of default arguments when calling
; a procedure. 
; This source also includes basic operations on the lists
; such as 'list 'cons 'car ' cdr implemented for problem # 1
;
; The only function modified for this problem is apply-proc
; the help functions used are : 
;  filter-formals, filter-args, filter-defs and eval-list

(load "/cs/cs2135/mitmacros.scm")

(define-record lit (datum))
(define-record varref (var))
(define-record lambda (formals body))
(define-record app (rator rands))
(define-record prim-proc (prim-op))
(define-record if (test-exp then-exp else-exp))
(define-record let (decls body))
(define-record decl (var exp))
(define-record closure (formals body env))

(define parse (lambda (datum)
 (cond
   ((number? datum) (make-lit datum))
; following line was modified from the original code:
   ((eq? datum 'emptylist) (make-lit '()))
   ((symbol? datum) (make-varref datum))
   ((pair? datum)
    (cond	((eq? (car datum) 'lambda)
                (make-lambda 	(cadr datum) 
						(parse (caddr datum))))
		((eq? (car datum) 'if)
		 (make-if 	(parse (cadr datum)) 
					(parse (caddr datum))
					(parse (cadddr datum))))
            	((eq? (car datum) 'let)
		 (make-let 	(map do-make-decl (cadr datum)) 
					(parse (caddr datum))))
 		(else
                (make-app 	(parse (car datum)) 
					(map parse (cdr datum))
	))))
      (else (error "parse: Invalid concrete syntax" datum))
)))

(define do-make-decl (lambda (x) 
	(make-decl (car x) (parse (cadr x)) )))
(define list-index (lambda (s los)
	(help-list-index 0 s los)))
(define help-list-index (lambda (so-far s los)
	(if 	(null? los)
		-1
		(if	(eq? s (car los))
			so-far
			(help-list-index (+ 1 so-far) s (cdr los))))))
(define ribassoc (lambda (s los v fail-value)
	(if (eq? -1 (list-index s los)) 
			fail-value 
			(vector-ref v (list-index s los)))))

(define create-empty-ff  (lambda () ()))

(define extend-ff* (lambda (sym-list val-list ff) 
 	(cons (cons sym-list (list->vector val-list)) ff)))

(define apply-ff (lambda (ff symbol) 
  ( if (null? ff) (error  "no association for symbol"  symbol)
	 (let    (  (answer (ribassoc symbol (caar ff) (cdar ff) 'fail)) )
	      (if (eq? answer 'fail) (apply-ff (cdr ff) symbol) answer ) ) )))

(define the-empty-env (create-empty-ff))
(define extend-env extend-ff*)
(define apply-env apply-ff)

(define run (lambda (x) (eval-exp (parse x) init-env)))

(define read-eval-print (lambda ()
	(display "-->  ")
	(write (eval-exp (parse (read)) init-env))
	(newline)
	(read-eval-print)))

(define apply-prim-op
  (lambda (prim-op args)
    (case prim-op
      ((+) (+ (car args) (cadr args)))
      ((-) (- (car args) (cadr args)))
      ((*) (* (car args) (cadr args)))
      ((add1) (+ (car args) 1))
      ((sub1) (- (car args) 1))

; following lines were modified from the original code:
; ********************************************************
      ((cons) (cons (car args) (cadr args)) )
      ((car) (caar args) )
      ((cdr) (cdar args) )
      ((list) args )
      ((emptylist) '())
; ********************************************************
      (else (error "Invalid prim-op name:" prim-op))))) 

; following line was modified from the original code:
(define prim-op-names '(+ - * add1 sub1 cons car cdr list emptylist)) 
            
(define init-env 
   (extend-env
  	prim-op-names
    	(map make-prim-proc prim-op-names)
    the-empty-env))


(define eval-exp
  (lambda (exp env)
    (variant-case exp
      (lit (datum) datum)
      (varref (var) (apply-env env var))
      (app (rator rands)
        (let ((proc (eval-exp rator env))
              (args (eval-rands rands env)))
          (apply-proc proc args)))
      (if (test-exp then-exp else-exp)
        (if (true-value? (eval-exp test-exp env))
            (eval-exp then-exp env)
            (eval-exp else-exp env)))
      (let (decls body)
        (let ((vars (map decl->var decls))
              (exps (map decl->exp decls)))
         (let ((new-env (extend-env vars
                           (eval-rands exps env)
                           env)))
            (eval-exp body new-env))))
      (lambda (formals body)
        (make-closure formals body env))
      (else (error "Invalid abstract syntax:" exp)))))

(define eval-rands
  (lambda (rands env)
    (map (lambda (rand) (eval-exp rand env))
         rands)))

(define true-value?
  (lambda (x)
    (not (zero? x))))

; following 2 definitions were added to original code
; ********************************************************

; filter-formals returns all symbols top and second level
;  bublist of the list flst:
;  filter-formals '(a b (c 4) (d 5)) -> (a b c d)
(define filter-formals (lambda (flst) (
  cond 
	((null? flst) '())
	((symbol? (car flst)) (cons (car flst) 
			           (filter-formals (cdr flst))
                             ))
	(else (cons (caar flst) (filter-formals (cdr flst))) )
)))


; filter-defs retuens all second elements from sublist that
; are elements of the main list lst:
; filter-defs '((a 2) (b 3)) -> (2 3)
(define filter-defs (lambda (lst) (
cond 
 ((null? lst) '())
 (else (cons (cadar lst) (filter-defs (cdr lst)))
 )
)))


; filter-args returns list of actuall values that will be assigned
; to list of formals
(define filter-args (lambda (flst alst) (
  cond
   ((null? flst) '())
   ((null? alst) (filter-defs flst) )
   ((symbol? (car flst))
        (cons (car alst) (filter-args (cdr flst) (cdr alst)))
   )
   ((list? (car flst))
        (if (null? alst) (cons (cadar flst)
                               (filter-args (cdr flst) (cdr alst))
                         )
                         ; else
                         (cons (car alst)
                               (filter-args (cdr flst) (cdr alst))
                         )
        )
   )
)))


; eval-list evaluates a list of formals:
; eval-list (eval-list '(a b (d 4) (e (+ 4 5)))) -> (a b (d 4) (e 9))
(define eval-list (lambda (lst) (
 cond
 ((null? lst) '())
 ((symbol? (car lst)) 
   (cons (car lst)
         (eval-list (cdr lst))
   )
 )
 (else 
   (cons (list (caar lst) 
	       (eval-exp (parse (cadar lst)) init-env)                 	  
	 )
         (eval-list (cdr lst))
	 
   )
 )
)))

; following definition was modified from the original code:
; ********************************************************

(define apply-proc
  (lambda (proc args)
    (variant-case proc
      (prim-proc (prim-op) (apply-prim-op prim-op args))
      (closure (formals body env)
        (eval-exp body 
		(extend-env (filter-formals (eval-list formals))
			    (filter-args (eval-list formals) args)
			     env
                )
        ))
      (else (error "Invalid procedure:" proc)))))
