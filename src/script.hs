data IntList = Nil
             | Cons Int IntList

Nil :: IntList
Cons :: Int -> IntList -> IntList
Cons 3 :: IntList -> IntList
Cons 3 Nil :: IntList

empty_list = Cons 3 (Cons 4 Nil)

is_odd :: Int -> Bool
is_odd x = (x `mod` 2) /= 0


       (%) = mod

x % y = x `mod` y

plus
+

2 + 3
plus 2 3
2 `plus` 3
(+) 2 3



data Bool = False | True

length Nil = 0
length (Cons x xs) | is_odd x = length xs
                   | sausage = 1 + length xs

 length (Cons 3 (Cons 4 Nil))
== 1 + length (Cons 4 Nil)
== 1 + (1 + (length Nil))
== 1 + (1 + (0))


data List a = Nil
            | Cons a (List a)

flatten :: List (List b) -> List b
flatten Nil = Nil
flatten (Cons x xs) = x ++ flatten xs

(++) :: List a -> List a -> List a
Nil ++ y = y
Cons x xs ++ y = Cons x (xs ++ y)
        -- x :: a
        -- xs :: List a
        -- y :: List a
        -- want :: List a
