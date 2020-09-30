# File-Management-Language-DSL

## File Management Language - Grammar

### A sample script

For all files under `/some/folder`, if the file is greater than 50 mb, move it to `/other/folder`:

```

someFolder = '/some/folder'
otherFolder = '/other/folder'

filesToCheck[] = someFolder -> '*'

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
Copy
Move
Delete
Rename
Create
Compress # ?
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
Create myFolder

# Create myFile throws error. Cannot create files.
```

### Define multiple actions

```
myFolder = '/some/folder'
otherFolder = '/some/otherFolder'

Create myFolder
copy myFolder to otherFolder
```

### Define a conditional action

```
if (file.size > 50 MB) {         # can be 'bytes', 'KB', 'MB', 'GB', 'TB', 'PB'
    move file to otherFolder
}
```

### Grammar

```
PROGRAM ::= STATEMENT*
STATEMENT ::= ACTION | IF_STATEMENT | FOR_LOOP | DECLARATION | RETURN

ACTION ::= COMMAND FILE_VARIABLE (PREPOSITION ACTION_DESTINATION)?
IF_STATEMENT ::= 'if(' CONDITION ') {' PROGRAM '}'
FOR_LOOP ::= 'for(' ITERATOR ') {' PROGRAM '}'
DECLARATION ::= DIRECT_DECLARATION | REFERENCE_DECLARATION | RELATIVE_DECLARATION
RETURN ::= 'return'

COMMAND ::= 'Copy' | 'Move' | 'Delete' | 'Rename' | 'Create' | 'Compress'
PREPOSITION ::= 'to' | 'into'
ACTION_DESTINATION ::= [FILE_VARIABLE | FILENAME]

FILE_VARIABLE ::= [a-zA-Z0-9]+
LIST_VARIABLE ::= FILE_VARIABLE '[]'
VARIABLE ::= FILE_VARIABLE | LIST_VARIABLE

CONDITION ::= OR_CONDITION
OR_CONDITION ::= AND_CONDITION ('OR' AND_CONDITION)*
AND_CONDITION ::= NEGATION ('AND' NEGATION)*
NEGATION ::= (NEGATION_OPERATOR NEGATION) | BOOLEAN
BOOLEAN ::= 'True' | 'False' | COMPARISON | CONDITION | '(' BOOLEAN ')'
COMPARISON ::= TERM COMPARISON_OPERATOR TERM
TERM ::= NUMBER | (NUMBER UNIT)
NUMBER ::= [0-9]+
UNIT :: = 'B' | 'KB' | 'MB' | 'GB' | 'TB' | 'PB'

NEGATION_OPERATOR ::= 'NOT' | '!'
COMPARISON_OPERATOR := '<' | '>' | '==' | '>=' | '<=' | '<>' | '!='

ITERATOR ::= FILE_VARIABLE 'in' LIST_VARIABLE

DIRECT_DECLARATION ::= VARIABLE '=' PATH
REFERENCE_DECLARATION ::= VARIABLE '=' VARIABLE
RELATIVE_DECLARATION ::= VARIABLE '->' PATH

PATH ::= ABSOLUTE_PATH | RELATIVE_PATH
ABSOLUTE_PATH ::= DRIVENAME RELATIVE_PATH?
DRIVENAME ::= [A-Z] ':\'
RELATIVE_PATH ::= FILENAME | (FILENAME '\' RELATIVE_PATH)
FILENAME ::= [0-9a-zA-Z\_\-\.\s]+
```
