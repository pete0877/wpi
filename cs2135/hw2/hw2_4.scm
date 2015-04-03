; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #4
;   this function searches through a binaty tree lst, and looks for the
; path R/L to the element n . If the element is not in the tree, then 
; the program returns 'error

(define path (lambda (n lst) (
  if (null? lst) 'error
  ; ELSE
  (
        if (eq? (car lst) n) '()
        (
	   if (> (car lst) n)
	   ; try left subtreee:
	   (
		 if (eq? (path n (cadr lst)) 'error) 'error 
		   (cons 'L (path n (cadr lst)))
	   )
	   ; Else  try right subtree
	   (
		 if (eq? (path n (caddr lst)) 'error) 'error 
		   (cons 'R (path n (caddr lst)))
	   )                
        )
  )
)))
