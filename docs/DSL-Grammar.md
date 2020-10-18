# File Management Language - Grammar

### A sample script

For all files under `/some/folder`, if the file is greater than 50 mb, move it to `/other/folder`:

```

someFolder = '/some/folder'                  # paths are either absolute or relative to the location of the script file
otherFolder = '/other/folder'

filesToCheck[] = someFolder -> '*'           # wildcards allow creating a list with all files that match pattern

for (file in filesToCheck[]) {
    if (file.size > 50 MB) {         # can be 'B', 'KB', 'MB', 'GB', 'TB', 'PB'
        move file to otherFolder
    }
}
```

### Reference a file or folder

Reference a folder:

```
myFolder = '/path/to/some/folder'                 # stores a reference to folder in variable myFolder
```

Reference a file

```
myFile = '/path/to/some/file.ext'                  # stores a reference to file.ext in variable myfile
```

Reference a file relative to a folder

```
myFile = myFolder -> 'file.ext'                    # stores a reference to myFolder/file.ext
```

Reference multiple files and folders

```
subFilesAndFolders[] = '/path/to/some/parent/*'      # subFilesAndFolders[] is a list of all files and folders under /parent


# Alternatively

parent = '/path/to/some/parent'
subFilesAndFolders[] = parent -> '*'                 # subFilesAndFolders[] is a list of all files and folders under /parent
```

### Access file/folder attributes

```
myFile.name            # returns file name
myFile.size            # returns file size in bytes
myFile.created         # returns UNIX timestamp of creation
myFile.modified        # returns UNIX timestamp of last modification
myFile.extension       # returns the extension as a string. eg. 'exe'. '' for folders.
myFile.parent          # returns reference to parent folder
myFile.isFile          # boolean value. True if myFile is a file.
myFile.isDirectory     # boolean value. True if myFile is a directory/folder
```

### Define a condition

```
if (some condition) {
    # enters block on condition being true
}
```

### Define multiple conditions

```
if (some condition) {
    # enters block on condition being true
    # action here
    if (some other condition) {
        # enters block on second condition being true
    }
}

if (some condition and some condition) {
}
```

OR

```
if ((some condition) AND (some other condition)) {       # AND, OR, NOT
    # enters block on both conditions being true
}
```

### Define an action

Action format:

```
<command> <target> (to | into <destination>)?
```

All commands:

```
copy
move
delete
rename
create
compress
```

Copy a file:

```
copy someFile to otherFolder      # special cases: overwrite?
```

Delete a file:

```
delete someFile
```

Create a folder:

```
myFolder = '/some/folder'
create myFolder

# Create myFile throws error. Cannot create files.
```

### Define multiple actions

```
myFolder = '/some/folder'
otherFolder = '/some/otherFolder'

create myFolder
copy myFolder to otherFolder
```

### Define a conditional action

```
if (file.size > 50 MB) {         # can be 'B', 'KB', 'MB', 'GB', 'TB', 'PB'
    move file to otherFolder
}
```

### Grammar

```
PROGRAM ::= STATEMENT*
STATEMENT ::= ACTION | IF_STATEMENT | FOR_LOOP | DECLARATION | RETURN

ACTION ::= COMMAND FILE_VARIABLE (PREPOSITION ACTION_DESTINATION)?
IF_STATEMENT ::= 'if(' CONDITION ') {' PROGRAM '}' ('else' '{' PROGRAM '}')?
FOR_LOOP ::= 'for(' ITERATOR ') {' PROGRAM '}'
DECLARATION ::= DIRECT_DECLARATION | REFERENCE_DECLARATION
RETURN ::= 'return'

COMMAND ::= 'copy' | 'move' | 'delete' | 'rename' | 'create' | 'compress'
PREPOSITION ::= 'to'
ACTION_DESTINATION ::= [FILE_VARIABLE | ('"' FILENAME '"')]

FILE_VARIABLE ::= [a-zA-Z0-9]+
LIST_VARIABLE ::= FILE_VARIABLE '[]'
VARIABLE ::= FILE_VARIABLE | LIST_VARIABLE

CONDITION ::= OR_CONDITION
OR_CONDITION ::= AND_CONDITION ('OR' AND_CONDITION)*
AND_CONDITION ::= NEGATION ('AND' NEGATION)*
NEGATION ::= (NEGATION_OPERATOR NEGATION) | BOOLEAN
BOOLEAN ::= 'True' | 'False' | COMPARISON | CONDITION | '(' BOOLEAN ')'
COMPARISON ::= TERM COMPARISON_OPERATOR TERM
TERM ::= ATTRIBUTE | (NUMBER (UNIT)?) | STRING | BOOLEAN
ATTRIBUTE ::= FILE_VARIABLE '.' ATTRIBUTE_NAME
ATTRIBUTE_NAME ::= 'name' | 'size' | 'created' | 'modified' | 'extension' | 'parent' | 'isFile' | 'isDirectory'
NUMBER ::= [0-9]+
UNIT :: = 'B' | 'KB' | 'MB' | 'GB' | 'TB' | 'PB'

NEGATION_OPERATOR ::= 'NOT' | '!'
COMPARISON_OPERATOR := '<' | '>' | '==' | '>=' | '<=' | '!='

ITERATOR ::= FILE_VARIABLE 'in' LIST_VARIABLE

DIRECT_DECLARATION ::= VARIABLE '=' PATH
REFERENCE_DECLARATION ::= VARIABLE '=' VARIABLE

PATH ::= ABSOLUTE_PATH | RELATIVE_PATH | VARIABLE '->' PATH
ABSOLUTE_PATH ::= DRIVENAME RELATIVE_PATH?
RELATIVE_PATH ::= FILENAME | (FILENAME '\' RELATIVE_PATH)

DRIVENAME ::= [A-Z] ':\'
FILENAME ::= [\*0-9a-zA-Z\_\-\.\s]+
```

### Validation Rules

These are rules that check for syntactically correct but semantically wrong inputs.

These rules should be checked **in addition** to checking that the program meets the syntax above. Syntax rules are not repeated below.

Validation rules are static (i.e. does not check if files/folders exist) and are checked prior to evaluation

#### PROGRAM

1. Variables declared before usage for all statements
2. Each statement is independently valid

#### ACTION

1. Command is valid
2. File variable is valid
3. If Command is one of 'copy' or 'move' then destination is a file variable
4. If Command is 'rename' then destination is a file name
5. If Command is 'delete', 'create' or 'compress' then no preposition and destination are given

#### IF_STATEMENT

1. Condition is valid
2. First Program is valid. Second Program is null or valid.

#### FOR_LOOP

1. Iterator is valid
2. Program is valid

#### COMPARISON

1. Both terms should evaluate to the same type
2. If the operator is not '==' or '!=', then the terms must evaluate to numbers

#### ITERATOR

1. File variable must not have been declared before

#### DIRECT_DECLARATION

1. If path contains wildcard ('*') then variable must be list variable

#### REFERENCE_DECLARATION

1. Variable on right must have been declared before

#### PATH

1. In the VARIABLE '->' PATH case, the variable must have been declared before






