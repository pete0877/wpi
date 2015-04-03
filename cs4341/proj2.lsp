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
;-------------------------------------------------------------------
(defun like_score_range (score) (
; If score is 1-3 return NOTLIKE, else LIKE
    if (< score 4) 'notlike 'like
))
;-------------------------------------------------------------------
(defun rater_profile (raterID profiles) (
; Returns list of rater's answers to all the questions                                         
  if (eq profiles '()) '()
  (
     if (eq (caar profiles) raterid) (cadar profiles)      
        (rater_profile raterid (cdr profiles))
  )                                    
))
;-------------------------------------------------------------------
(defun did_rater_rate_item (raterID itemID) (
; T - yes, NIL = no                                      
    not (eq 'NA (rating raterID itemID))                                             
))
;-------------------------------------------------------------------
(defun all_rater_IDs_HELPER (lst) (
   if (eq lst '()) '()
   ( cons (caar lst) (all_rater_IDs_HELPER (cdr lst)))
))
;-------------------------------------------------------------------
(defun all_rater_IDs () (
; Returns list of all reter IDs in the data file
   all_rater_IDs_HELPER (cadr input_data) 
))
;-------------------------------------------------------------------
(defun raters_that_rated_item_HELPER (itemID all_raters) (
   if (eq all_raters '()) '()
   (if (did_rater_rate_item (car all_raters) itemID)
      (cons (car all_raters) (raters_that_rated_item_HELPER itemID (cdr all_raters)))
      (raters_that_rated_item_HELPER itemID (cdr all_raters))    
   )
))
;-------------------------------------------------------------------
(defun raters_that_rated_item (itemID) (
; List of IDs of raters that gave rating to the item
   raters_that_rated_item_HELPER itemid (all_rater_ids)
))
;-------------------------------------------------------------------
(defun attributes_list () (
; List of possible attributes                           
    caddar demograph-set
))
;-------------------------------------------------------------------
(defun raters_profiles (raters itemid) (
; Returns list of raters profiles in the list along with their rating (LIKE / DISLIKE)
;  given to the Item                                        
   if (eq raters '()) '()
   (cons (list (rater_profile (car raters) 
                              (cadr demograph-set)   
               ) 
               (like_score_range (rating (car raters) itemid))  
         )      
         (raters_profiles (cdr raters) itemid)
   )                              
))
;-------------------------------------------------------------------
(defun kill_NIL_profiles (lst) (
; Small help function that corrects the situation when 
; certain raters rated items, but didn't fill the survey                                 
   if (eq lst '()) '()
   (if (eq (caar lst) '())
     (kill_NIL_profiles (cdr lst))      
     (cons 
        (car lst) 
        (kill_NIL_profiles (cdr lst))
     )
   )                              
))
;-------------------------------------------------------------------
(defun killAllNILS (what) (
; Small help function used to delete all the NILs from the learning tree                           
    if (eq what '()) '()
    (
      if (eq (car what) 'NIL) (killAllNILS (cdr what))
       (if (listp (car what))  
          (cons (killAllNILS  (car what)) (killAllNILS (cdr what)))
          (cons (car what) (killAllNILS (cdr what)))
       )        
    )                           
))
;-------------------------------------------------------------------
(defun rater_answers (RaterID) (
; Returns list in format ((ATR1 ATR2 ATR3) (VAL1 VAL2 VAL3))
; where VARN is the answer to question ATRN given by the Rater                               
    list (attributes_list) (rater_profile RaterID (cadr demograph-set))                                
))
;-------------------------------------------------------------------
(defun learntree (node) 
; This function constructs and returns the full decision tree
; using the initial node      
 '(cook   
   ( 
      (good (calories ( (yes  (like)) (no   (notlike))  )))
      (fair (calories ( (yes  (like)) (no   (notlike))  )))    
      (bad  (unknown))        
   )
  )
)
;-------------------------------------------------------------------
(defun find_child (possible_children answer) (
; Small navigational tool that decides which way to go in the decision
; tree based on the given answer      
   if (eq possible_children '()) '(unknown)
   (
    if (eq answer (caar possible_children)) (cadar possible_children)
    (find_child (cdr possible_children) answer) 
   )
))
;-------------------------------------------------------------------
(defun raters_answer (question answers) (
; Returns answer to the question. The answer is in somewhere in the
; answers list. We just have to find it.                                                                                  
    nth_list_element 
     (cadr answers)                                            
     (find_element_index (car  answers) question)                                          
))
;-------------------------------------------------------------------
(defun makeInitialNode (ExampleL remainingAttrL) (
; Start node that is used to construct the learning tree
    list 'NIL 'NIL 'NIL ExampleL remainingAttrL
))
;-------------------------------------------------------------------
(defun printTree (tree) (
; This function prints the learning tree
    print tree
))
;-------------------------------------------------------------------
(defun assembleExampleL (itemID) (
; Returns list of examples to be used when contructing the learning tree                                                                    
; Each tree will depend on the Item that we care about                                  
   cons (attributes_list)                                  
        (list 
           (
             kill_NIL_profiles (raters_profiles (raters_that_rated_item itemID) itemID)
           )
        )
))
;-------------------------------------------------------------------
(defun classify (tree answers) (
; Maps the answers onto the decision tree, to decide what's the 
; final decision:                                                                
; (car  tree)   - current attribute question
; (car  answers) - attributes 
; (cadr answers) - values
; (raters_answer (car  tree) answers) - answer to current question
; (find_child (cadr tree) (raters_answer (car  tree) answers)) 
;    - next node after answering to the current question.                                 
;    if equal to '(like) or '(dislike) or '(unknown)
;    then we dont need to ask any more questions                                
                                
   if (eq '() 
           (cdr 
             (find_child (cadr tree) (raters_answer (car  tree) answers))
           )
      )
      (car 
         (find_child (cadr tree) (raters_answer (car  tree) answers)) 
      )
      (classify
         (find_child (cadr tree) (raters_answer (car  tree) answers)) 
         answers        
      )                                  

))
;-------------------------------------------------------------------
(defun predictRatingsT (RaterID ItemID) (
; Predicts rater's opinion about the item based on the decision tree
     classify (
               killallnils
                (learnTree 
                 (makeInitialNode 
                     (assembleExampleL itemID)
                     attribute-vals)
                )
              )
              (rater_answers raterid)                                         
))
;-------------------------------------------------------------------
