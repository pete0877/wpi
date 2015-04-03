; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #4
;  This program takes number n and expression exp and produces list 
; with n exp elements

(define Duple (lambda (n expr) (
	if (eq? n 0) '()
	; ELSE
	(cons expr (Duple (- n 1) expr))
)))
