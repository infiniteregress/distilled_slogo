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
            "body": "\\w+",
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
            "pattern": ["unaryOperation", "constant"],
            "parent": "0",
            "grandparent": "result"
        },
        {
            "pattern": ["unaryOperation", "result"],
            "parent": "0",
            "grandparent": "result"
        }
    }

Each grammar rule is a JSON object with several attributes:
-   "pattern" describes a sequence of labels that will match this pattern. This attribute is required. Any regex pattern is supported for each entry in this sequence. Also, the "\_infinite" label is a special label which will match one or more repetitions of the previous label in the sequence.
-   "parent" describes the node to nest the rest of the pattern under. This is either an index to a particular label in the pattern, or the name of a new tree node to create. This attribute is required.
-   "grandparent" describes the node to nest the parent under, if any. The same semantics as "parent" apply. This attribute is optional, and if not set, or set to "", then the parent will not be nested further.

Then, you can load these two files using their corresponding file loader utility classes:

    TokenRuleLoader tokenLoader = new TokenRuleLoader("./token_rules.txt");
    List<ITokenRule> tokenRules = tokenLoader.getRules();

    GrammarRuleLoader<String> grammarLoader = new GrammarRuleLoader<>("./parsing_rules.txt");
    List<IGrammarRule<String>> grammarRules = grammarLoader.getRules();

Then, you can create a new Tokenizer and Parser classes using these rules:

    ITokenizer tokenizer = new Tokenizer(tokenRules);
    IParser<String> parser = new Parser<>(grammarRules, new StringOperationFactory);

To generate the tree, simply call:

    String command = "@run 50";
    ISyntaxNode<String> parseTree = parser.parse(tokenizer.tokenize(new StringReader(command)));

To traverse the tree and retrieve the operation associated with a particular node, respectively, use:

    List<ISyntaxNode<String>> children = parseTree.children();
    String operation = parseTree.operation();

