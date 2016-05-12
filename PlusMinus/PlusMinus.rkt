#lang r5rs

(define plus-minus
  (lambda (list sum target)
    (cond
      ;Base case: returns true based on whether the target value and sum are equal.
      ((null? list) (= sum target))

      ;Recursive case
      ( else
        (cond
          (
           ;At the head of the list
           ;The head node's value is set as the target parameter.
           (= target 0)
            (cond
              ;First call: adds the value at the current node to the running total
              ((plus-minus (cdr list) (+ sum (car list)) (car list)) #t)
              ;Second call: subtracts that value from the running total
              ((plus-minus (cdr list) (- sum (car list)) (car list)) #t)

              ;Returns false if neither of the above calls returns true
              (else #f)
             )
           )

          ;For nodes after the head 
          (else
           (cond
             ;For the first two calls, the previous target value is submitted as the target value.
             ;This is necessary in order to compare values at nodes in the beginning of the
             ;list with the eventual plus-minus total.
             ;First call: adds the value at the current node to the running total
             ((plus-minus (cdr list) (+ sum (car list)) target) #t)
             ;Second call: subtracts the value at the current node from the running total
             ((plus-minus (cdr list) (- sum (car list)) target) #t)

             ;For the second two calls, the current node's value is submitted as the target value.
             ;Third call: adds the value at the current node to the running total
             ((plus-minus (cdr list) (+ sum (car list)) (car list)) #t)
             ;Fourth call: subtracts the value at the current node from the running total
             ((plus-minus (cdr list) (- sum (car list)) (car list)) #t)

             ;Returns false if none of the above calls returns true
             (else #f)
             )
           )
          )
        )     
      )
    )
  )

