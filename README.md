distilled_slogo
===============

A library for generating parse trees from a list of tokenization and parsing rules

To compile from source, run "mvn clean package". This will create the distilled_slogo jars, including documentation and source jars, inside the target/ directory

To get started, include json-schema-validator, json-schema-validator-lib, and json jars in your build path. Then, add the distilled-slogo jar to your build path.

Dependencies
------------

com.github.fge.json-schema-validator, recommended version 2.2.6

org.json.json, recommended version 2014113

junit.junit, for testing, recommended version 4.11

Overview
--------

In order to get from a string of characters to a parse tree, distilled_slogo first transforms the string into a list of atomic chunks, called tokens. In the process, distilled_slogo labels each chunk using an abstract name, such as "constant". These chunks are then nested under each other based on certain rules. Eventually, if the input is syntatically correct, a tree with a single root node will be generated. This root node is the root of the parse tree.

HowTo
-----
First, define your tokenization and parsing rules in two separate JSON files.

The format for the tokenization rules is:

    [
        {
            "label": "constant",
            "body": "[0-9]+",
            "opening": "\\W+",
            "closing": "\\W+"
        },
        {
            "label": "unaryOperation",
            "body": "\\@\\w+",
            "opening": "\\W+",
            "closing": "\\W+"
        }
    ]

Each token rule is a JSON object with several attributes:
-   "label" describes the name that tokens created using this rule will take. This attribute is required.
-   "body" describes the regex pattern matching the actual text of the token. This attribute is required.
-   "opening" describes the regex delimiting the start of the token. This attribute is optional and defaults to "\\s+", i.e. one or more whitespace characters.
-   "closing" describes the corresponding regex for the end. This attribute is optional and defaults to "\\s+".

The format for the grammar rules is:

    [
        {
            "pattern": [
                {"label": "unaryOperation", "level": 1},
                {"label": "constant|result", "level": 0}
            ],
            "additional": [
                {"label": "result", "level": 2}
            ]
        }
    }

Each grammar rule is a JSON object with several attributes:
-   "pattern" describes a sequence of labels that will match this pattern. This attribute is required. Each element in "pattern" can contain three attributes:
  -   "label": The regex pattern to match for for this element. This attribute is required.
  -   "level": The integer level to nest this element at. This attribute is required. A special value of -1 indicates the element should not be included in the generated tree. 0 indicates that the element will be a leaf of the tree. Any value greater than 0 indicates that the element will be a parent of the level one less than the element's level and a child of the level one greater. There must be at most one element at each level above 0. Values less than -1 are not allowed.
  -   "repeating": A boolean describing whether this particular element repeats one or more times. This attribute is optional.
-   "additional" describes the additional nodes to create during nesting. This attribute is optional. The entries for "additional" have the same format as those for "pattern".

Then, you can load these two files using their corresponding file loader utility classes:

    TokenRuleLoader tokenLoader = new TokenRuleLoader("./token_rules.txt");
    List<ITokenRule> tokenRules = tokenLoader.getRules();

    GrammarRuleLoader<String> grammarLoader = new GrammarRuleLoader<>("./parsing_rules.txt");
    List<IGrammarRule<String>> grammarRules = grammarLoader.getRules();

Then, you can create new Tokenizer and Parser classes using these rules:

    ITokenizer tokenizer = new Tokenizer(tokenRules);
    IParser<String> parser = new Parser<>(grammarRules, new StringOperationFactory);

The second argument to the Parser's constructor indicates the factory used to create new instances of a generic object from the token's label at each node. Distilled_slogo provides a basic StringOperationFactory which merely echoes the token's label into each node's "payload". If you have factories used to instantiate particular "command" objects at each node (i.e. you have Strategy classes), you may wrap those factories using the IOperationFactory interface.

To generate the tree, simply call:

    String command = "@run 50";
    ISyntaxNode<String> parseTree = parser.parse(tokenizer.tokenize(new StringReader(command)));

To traverse the tree and retrieve the operation associated with a particular node, respectively, use:

    List<ISyntaxNode<String>> children = parseTree.children();
    String operation = parseTree.operation();

