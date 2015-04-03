;-------------------------------------------------------------------
; Peter Golaszewski
; Tim Obrien
; Team: "Pink Bunny"
; CS4341 B98 Project 2
; 11/26/98
;-------------------------------------------------------------------

; Load the initial data and the previous project file
(load "demograph.lsp")
(load "proj1.lsp")

; If score is 1-3 return NOTLIKE, else LIKE
(defun like_score_range (score) 
   (if (< score 4) 'notlike 'like))

; Returns list of rater's answers to all the questions                                         
(defun rater_profile (raterID profiles) 
   (if (eq profiles '()) 
      '() 
      (if (eq (caar profiles) raterid) 
         (cadar profiles) 
         (rater_profile raterid (cdr profiles)))))

; T - yes, NIL = no                                      
(defun did_rater_rate_item (raterID itemID) 
   (not (eq 'NA (rating raterID itemID))))

(defun all_rater_IDs_HELPER (lst) 
   (if (eq lst '()) 
      '()
      (cons (caar lst) (all_rater_IDs_HELPER (cdr lst)))))

; Returns list of all rater IDs in the data file
(defun all_rater_IDs () 
   (all_rater_IDs_HELPER (cadr input_data)))

(defun raters_that_rated_item_HELPER (itemID all_raters) 
   (if (eq all_raters '()) 
      '()
      (if (did_rater_rate_item (car all_raters) itemID)
         (cons (car all_raters) (raters_that_rated_item_HELPER itemID (cdr all_raters)))
         (raters_that_rated_item_HELPER itemID (cdr all_raters)))))

; List of IDs of raters that gave rating to the item
(defun raters_that_rated_item (itemID) 
   (raters_that_rated_item_HELPER itemid (all_rater_ids)))

; List of possible attributes                           
(defun attributes_list () 
   (caddar demograph-set))

; Returns list of raters profiles in the list along with their rating (LIKE / DISLIKE)
;  given to the Item                                        
(defun raters_profiles (raters itemid) 
   (if (eq raters '()) 
      '()
      (cons 
        (list (rater_profile (car raters) (cadr demograph-set))
              (like_score_range (rating (car raters) itemid)))
        (raters_profiles (cdr raters) itemid))))

; Small help function that corrects the situation when 
; certain raters rated items, but didn't fill the survey                                 
(defun kill_NIL_profiles (lst) 
   (if (eq lst '()) 
      '()
      (if (eq (caar lst) '())
         (kill_NIL_profiles (cdr lst))      
         (cons (car lst) (kill_NIL_profiles (cdr lst))))))

; Returns list in format ((ATR1 ATR2 ATR3) (VAL1 VAL2 VAL3))
; where VARN is the answer to question ATRN given by the Rater                               
(defun rater_answers (RaterID)
   (list (attributes_list) (rater_profile RaterID (cadr demograph-set))))

; Small navigational tool that decides which way to go in the decision
; tree based on the given answer      
(defun find_child (possible_children answer)
   (if (eq possible_children '()) 
      '(unknown)
      (if (eq answer (caar possible_children)) 
         (cadar possible_children)
         (find_child (cdr possible_children) answer))))

; Returns answer to the question. The answer is in somewhere in the
; answers list. We just have to find it.                                                                                  
(defun raters_answer (question answers) 
   (nth_list_element 
    (cadr answers)                                            
    (find_element_index (car  answers) question)))

; Start node that is used to construct the learning tree
(defun makeInitialNode (ExampleL remainingAttrL) 
   (list 'NIL 'NIL 'NIL ExampleL remainingAttrL))

; This function prints the learning tree
(defun printTree (tree) 
   (print tree))

; Returns list of examples to be used when contructing the learning tree                                                                    
; Each tree will depend on the Item that we care about                                  
(defun assembleExampleL (itemID) 
   (cons (attributes_list) (list (kill_NIL_profiles (raters_profiles (raters_that_rated_item itemID) itemID)))))

; Maps the answers onto the decision tree, to decide what's the 
; final decision:                                                                
; (car  tree)   - current attribute question
; (car  answers) - attributes 
; (cadr answers) - values
; (raters_answer (car  tree) answers) - answer to current question
; (find_child (cadr tree) (raters_answer (car tree) answers)) 
;    - next node after answering to the current question.                                 
;    if equal to '(like) or '(dislike) or '(unknown)
;    then we dont need to ask any more questions                                

(defun classify (tree answers)
   (if (eq '() (cdr (find_child (cadr tree) (raters_answer (car  tree) answers))))
      (car (find_child (cadr tree) (raters_answer (car  tree) answers)))
      (classify (find_child (cadr tree) (raters_answer (car  tree) answers)) answers)))

; Predicts rater's opinion about the item based on the decision tree
(defun predictRatingsT (RaterID ItemID) 
   (classify
    (learnTree (makeInitialNode (assembleExampleL itemID) attribute-vals))
    (rater_answers raterid)))

;--------------------------------------------------------------------------------------

(defun two-list-search (list-one list-two value-one)
   (cond ((or (endp list-one) (endp list-two)) nil)
         ((equal value-one (first list-one)) (first list-two))
         (t (two-list-search (cdr list-one) (cdr list-two) value-one))))

(defun countExamples (exampleL)
   (let ((remainingExampleL (cadr exampleL))
         (count 0))
      (loop
        (when (endp remainingExampleL) (return count))
        (setf remainingExampleL (rest remainingExampleL))
        (setf count (+ count 1)))))
        
(defun countLikes (exampleL)
   (let ((remainingExampleL (cadr exampleL))
         (count 0))
      (loop
        (when (endp remainingExampleL) (return count))
        (if (eq (cadar remainingExampleL) 'LIKE) (setf count (+ count 1)))
        (setf remainingExampleL (rest remainingExampleL)))))

(defun chooseAttr (exampleL remainingAttrL)
   (let ((totalExamples (countExamples exampleL))
         (remAttrL remainingAttrL)
         (curAttr ())
         (curAttrVals ())
         (lowestAttr ())
         (lowestAvgDisorder 1)
         (curAvgDisorder 0))
      (loop
        (when (endp remAttrL) (return lowestAttr))
        ;----Reset vars----
        (setf curAvgDisorder 0)
        (setf curAttr (caar remAttrL))
        (setf curAttrVals (cadar remAttrL))
        ;----Sub curAttr----
        (setf remAttrL (trimAttr curAttr remAttrL ()))
        (let ((valExampleL ())
              (curVal ())
              (numBranchExamples ())
              (numClassExamples 0)
              (disorderMultPart1 0)
              (disorderMultPart2 0)
              (temp1 0)
              (temp2 0))
           ;(print curAttr)
           (loop
             (when (endp curAttrVals) (return t))
             (setf curVal (car curAttrVals))
             ;(print curVal)
             (setf curAttrVals (rest curAttrVals))
             (setf valExampleL (trimExamples curAttr curVal exampleL))
             (setf numBranchExamples (countExamples valExampleL))
             (setf disorderMultPart1 0)
             (setf disorderMultPart2 0)
             (when (not (or (eq numBranchExamples 0) (eq numBranchExamples 1)))
                (setf numClassExamples (countLikes valExampleL))
                (when (not(eq numClassExamples numBranchExamples))
                   (progn
                     ;----portionOfWhole----
                     (setf disorderMultPart1 (/ numBranchExamples totalExamples))
                     ;----Like Class----
                     (setf temp1 (/ numClassExamples numBranchExamples))
                     (setf temp2 (/ (log temp1) (log 2)))
                     (setf disorderMultPart2 (+ disorderMultPart2 (* -1 temp1 temp2)))
                     ;----notLike Class----
                     (setf temp1 (/ (- numBranchExamples numClassExamples) numBranchExamples))
                     (setf temp2 (/ (log temp1) (log 2)))
                     (setf disorderMultPart2 (+ disorderMultPart2 (* -1 temp1 temp2)))
                     ;----add to curAvgDisorder across branches----
                     (setf curAvgDisorder (+ curAvgDisorder (* disorderMultPart1 disorderMultPart2)))))))
           ;(print curAvgDisorder)
           (when (< curAvgDisorder lowestAvgDisorder)
              (prog1 
                (setf lowestAvgDisorder curAvgDisorder)
                (setf lowestAttr curAttr)))))))


(defun allSameClass (exampleL)
   (let ((examples (cadr exampleL))
         (class (cadar (cadr exampleL))))
      (loop
        (when (endp examples) (return class))
        (if (not (eq class (cadar examples))) 
           (return NIL)
           (setf examples (rest examples))))))

         
(defun learnTree (node)
   (let ((exampleL (fourth node)))
      (if (allSameClass exampleL)
         (list (allSameClass exampleL))
         ;(list (allSameClass exampleL) NIL NIL NIL NIL)
         (splitNode node))))

(defun trimAttr (attribute remainingAttrL newAttrL)
   (cond ((endp remainingAttrL) newAttrL)
         ((not (eq (car (first remainingAttrL)) attribute)) 
          (trimAttr attribute (rest remainingAttrL) (append newAttrL (list (first remainingAttrL)))))
         (t (trimAttr attribute (rest remainingAttrL) newAttrL))))
      
;(setf tempAttrL ())

(defun splitNode (node)
   (let ((exampleL (fourth node))
         (remainingAttrL (fifth node))
         (newAttr ()))
      (setf newAttr (chooseAttr exampleL remainingAttrL))
      ;(setf tempAttrL (trimAttr newAttr remainingAttrL '()))
      ;(list NIL newAttr (partition newAttr exampleL) NIL NIL)))
      (list newAttr (partition newAttr exampleL (trimAttr newAttr remainingAttrL '())))))


(defun trimExamples (attribute value exampleL)
   (let ((attributes (car exampleL))
         (examples (cadr exampleL))
         (newExamples '()))
      (loop
        (when (endp examples) (return (list attributes newExamples)))
        (when (eq (two-list-search attributes (caar examples) attribute) value)
              (setf newExamples (append newExamples (list (car examples)))))
        (setf examples (rest examples)))))

(defun getAttrVals (attr)
   (cadr (assoc attr attribute-vals)))

(defun partition (attr exampleL remainingAttrL)
   (let ((attrVals (getAttrVals attr))
         (childExampleL ())
         (childrenL ())
         (newChild ()))
      (loop
        (when (endp attrVals) (return ChildrenL))
        (setf childExampleL (trimExamples attr (first attrVals) exampleL))
        (if (endp (cadr childExampleL))
           (setf newChild (list (first attrVals) (list 'unknown)))
           ;(setf newChild (list (first attrVals) (list 'unknown NIL NIL NIL NIL)))
           (setf newChild (list (first attrVals) (learnTree (list NIL NIL NIL childExampleL remainingAttrL)))))
        (setf childrenL (append childrenL (list newChild)))
        (setf attrVals (rest attrVals)))))
   
    
    
    