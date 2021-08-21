# NLP by SMMH

## Outline

### `Session`

- a `Session` stores anything "understood"
- a `Session` can open many files and load them into RAM as `Resource`s
- the state of a `Session` can be stored in a file and later loaded back into RAM to save progress, as it is computationally expensive

### `Decision`

- from its birth, every `Session` makes a great many `Decision`s to forge its "mind" and `Idea`s
- every `Decision` affects manys things
- every `Decision` must be reversible, and when reversed, its effects must completely vanish without a trace
- tough decisions may enter a queue and await user intervention
- the answer to a decision may be gray, not b/w

### `Document`

- a `Resource` may generate one or more `Documents`
- a `Document` is a `Tree` of `Idea`
- a `Document` is an `Idea` itself, so a document may have more documents as children

### `Idea`

## Hierarchical goals

- Guess language
- Analysis
  - normalize
  - tokenize
  - stopword removal
  - split sentences
    - informal vs. formal
    - spellcheck and correction
    - stemming and lemmatization
- Process/understanding
  - Contextual nuances [[w](https://en.wikipedia.org/wiki/Pragmatics)]
  - Rate words to gain insight
    - By importance, to find _key_-words
    - By sentiment, to accomplish sentiment analysis
    - By relavance, to find topics
      - Categorize and organize the documents
      - Generate a word-cloud
  - Extract entities
  - **innovations**
    - extract and store relations betweens words
    - deduce relations as statements and questions
    - deduce the lemmas of inflected words
  - stretch/compress
    - stretch
      - reword the same ideas
    - compress
      - delete less important ideas
      - delete repeated or similar ideas
      - summarize
- Synthesis
  - Orthography [[w](https://en.wikipedia.org/wiki/Orthography)]

## String analysis

- one-liner? (no line-breaks)
- human readable (ratio of textual symbols to length)
- well-broken (ratio of line-breaks to length)
- patternless (not CSV, TSV, or SSV)
- [plain](#plain-ness) (not JSON, Markdown, or XML)
  - XML tags
    - HTML tags
  - Markdown tags
    - Markdown headers
    - Markdown tables
    - Markdown emojis
    - Inline LaTeX
- well-distributed (unicode clusters)
- well-spaced (ratio of whitespace to length)
- well-separated (ratio of single spaces to length)
- well-structured (ratio of openers to closers)
- well-punctuated (ratio of punctuation to length)
- well-normalized (ratio of non-normal characters to length)
- well-articulated (clusters of repeated non-whitespace characters)
