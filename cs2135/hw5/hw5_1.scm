; Peter M. Golaszewski
; CS 2135, section 4
; Homework #5 problem #1 

;  This is modificated version of the interpreter,
; which allows to use basic operations on the lists
; such as 'list 'cons 'car ' cdr
; the only definitions modified are :
;  prime-op-names, apply-prime-op, and parse

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

(define apply-proc
  (lambda (proc args)
    (variant-case proc
      (prim-proc (prim-op) (apply-prim-op prim-op args))
      (closure (formals body env)
        (eval-exp body 
		(extend-env formals args env)))
      (else (error "Invalid procedure:" proc)))))
