# Compositionality

[[w](https://en.wikipedia.org/wiki/Principle_of_compositionality)]

"The meaning of a complex expression is determined by the meanings of its constituent expressions and the rules used to combine them."

## Defiants

- in context (vs. in isolation)
- idiomatic expressions
- quotations
- logical metonymies

Logical metonymies like "John began the book", where "to begin" subcategorizes an event as its argument, but "the book" is found instead.
This forces to interpret the sentence by inferring an implicit event ("reading", "writing", or other actions performed on a book)
The problem for compositionality is that the meaning of reading or writing is not present in the words of the sentence, neither in "begin" nor in "book".

| Theta roles   | Example                                     |
| ------------- | ------------------------------------------- |
| `CAUSE`       | The dog bit the child. This made him cry    |
| `AGENT`       | Joshua intentionally hit him                |
| `EXPERIENCER` | Sam hates cats/Josh noticed Alice           |
| `LOCATION`    | Marianne leaped through the field           |
| `GOAL`        | Moses gave Josh a toothbrush                |
| `BENEFICIARY` | Susie made cookies for Sarah                |
| `POSSESSOR`   | Shelly owns cats                            |
| `POSSESSED`   | Shelly's cats                               |
| `THEME`       | Josie sent Riven cookies                    |
| `IS`          | Josie is short; Sarah said that it is foggy |

```js
particular = [article, noun]
account = [particular, past, particular]
event = [particular, gerund, particular]

affix_ed = affix("-ed")
affix_ing = affix("-ing")
affix_ly = affix("-ly")

past(verb) = affixate(verb, affix_ed)
gerund(verb) = affixate(verb, affix_ing)
adverb(noun) = affixate(verb, affix_ly)

affixate("give", affix_ed) = "gave"
```
