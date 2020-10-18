# Project 1 - Mileston 4 (Team 4)

## Status of implementation

* We broke the implementation down to ~70 tasks
* We prioritized the tasks so as to have a basic functional prototype working first, and then add more features
* We're on track to have the first functional prototype ready by the end of the weekend

[See our task breakdown here](https://docs.google.com/spreadsheets/d/1urVh8VEvwH1IFHj-g6ipykEOhgiBH3P-MgAXKDAsfxw/edit?usp=sharing)

## Plans for final user study

* The final user study will be conducted late next week (around Oct 16th)
* The study will include tasks very similar to the initial user study, except users will be able to actually run the script
* Participants for the user study will be different from those in the initial study

## Planned timeline for the remaining days

* Goal for Oct 12th: have first functional prototype working
* Goal for Oct 16th: have remaining features implemented
* Oct 16th-18th: Part of the group will conduct user study, the other will create the presentation, and one person will be testing
* Oct 19th: All tasks complete, last minute tweaks.


# Project 1 - Milestone 3 (Team 4)

## Mockup of concrete language design (as used for your first user study)

[See this markdown for a full description of the proposed DSL Grammar](docs/DSL-Grammar.md)

## Notes about first user study

- Simple language to manage file system is very interesting, in particular the flexibility of mixing conditionals together and customize the flow of script
- It's strange to to not have "ELSE" or "if ELSE" when regular if statements are present
- Unclear whether the rename action parameter (the new name) requires quotation marks since defining the folder path does
- Clarify whether the program script user writes will be using relative paths to where the script exists or full paths must be specified
- "->" notation may not be intuitive, looks like folder into file
- Confusion over "to" vs. "into", should be equivalent
- Clarify that all paths must be abolute, no relative paths are supported (Windows based)

## Any changes to original language design

- Change upper case command names to lower to be consistent with examples shown

# Project 1 - Milestone 2 (Team 4)

## Roadmap and division of responsibilities

| Task                                                |       Estimated Timeline        |                                 How it will be divided                                  |
| --------------------------------------------------- | :-----------------------------: | :-------------------------------------------------------------------------------------: |
| Design and Planning                                 |                                 |                                                                                         |
| \_ Plan features for the langauge                   |           [Complete]            |                                       As a group                                        |
| \_ Define the Grammar                               | September 26th - September 27th |                                       As a group                                        |
| \_ Define format for User Study                     | September 26th - September 27th |                                       As a group                                        |
| \_ Conduct the User Study                           |  September 27th - October 2nd   | Each group member will conduct the study with 2-3 users based on the pre-defined format |
| \_ Adapt design to feedback                         |    October 3rd - October 4th    |                                       As a group                                        |
| Implementation                                      |                                 |                                                                                         |
| \_ Define the APIs and interfaces                   |    October 5th - October 6th    |                                       As a group                                        |
| \_ Define the classes and methods to be implemented |    October 6th - October 8th    |                                       As a group                                        |
| \_ Implement all components                         |   October 8th - October 16th    |                Individually. Components will be assigned to each member.                |
| Final Testing + User Study                          |                                 |                                                                                         |
| \_ Perform end-to-end testing                       |   October 16th - October 18th   |                Individually. Components will be assigned to each member.                |
| \_ Conduct User Study with final product            |   October 16th - October 18th   |                 Each group member will conduct a user study with 1 user                 |
| Launch + Presentation                               |                                 |                                                                                         |
| \_ Package the language for distribution            |   October 16th - October 19th   |                           Part of the team will work on this.                           |
| \_ Create the presentation                          |   October 16th - October 19th   |                           Part of the team will work on this.                           |

## Summary of progress so far

### Finalized DSL Idea

From Milestone 1:

> The Filesystem Organizer DSL allows the user to define rules and 'macros' for organizing files on their computer (either all across the entire hard drive or for specific folders), and corresponding actions to take when these rules are satisfied.

### Implementation decisions

- Programming language for DSL: Java

### Features Planned:

:heavy_check_mark: Support for Windows filesytems

:heavy_check_mark: Users can define conditions based on:

- Any file metadata such as
  - Name
  - File/Folder size
  - Date created/accessed
  - Owner
  - etc.
- Time/day at which script is run
- Location of the files

:heavy_check_mark: Users can define actions to take when conditions are satisfied, such as:

- Copy
- Move
- Delete
- Rename
- Partition (special move)
- Upload (POST)
- Change permissions (?)
- Compress

:heavy_check_mark: Conditions can be logically combined (AND, OR, NOT, XOR...)

:heavy_check_mark: Conditions and actions are chainable

# Project 1 - Milestone 1 (Team 4)

## Project idea: Filesystem Organizer DSL

## Overview

The Filesystem Organizer DSL allows the user to define rules and 'macros' for organizing files on their computer (either all across the entire hard drive or for specific folders), and corresponding actions to take when these rules are satisfied.

These rules could be based on the types of files, the file metadata, or even the time of the day/week/month. The corresponding actions based on the rule could include copying the files, deleting the files, moving the files, compressing the files, uploading the files to cloud, etc.

More advanced extensions to the DSL could include rules based on the contents of the files. For example, we could have rules to organize image files based on facial recognition.

There are two ways to design this DSL. Once the user defines the rules, our DSL could:

1. Run the rules once. To do the organizing again, the user would have to run the DSL commands again
2. The user triggers the DSL program once and it continuously runs in the background and listens for events (i.e. changes to the filesystem)

## Some tasks that could be accomplished with this DSL

1. Organize images by date
2. Automatically delete contents of the download folder every 30 days
3. If the size of a folder is greater than X GB, move half the contents to the cloud

## Samples of what the DSL might look like

The syntax and tokens used here is just for demo purposes, and the final DSL can use any syntax that is deemed best.

```
IN: "/my/folder"                            # defines actions for this folder

    ON: SIZE > 50GB                         # actions to do when size > 50 GB
        MOVE:                               # move all .jpeg files
            SRC: "*.jpeg"
            DST: "../../my/backup/folder/"

    ON: FREQUENCY  0 0 * * *                # actions to do at given cron
        DELETE:                             # delete all files
            SRC: "*"
```

## Discussion with TA and feedback received:

We shared with the TA all ideas we came up with during our discussion ([Miro Board](https://miro.com/welcomeonboard/OFPSshR6NVXc7lHtFjoofR2mxyfNP0fIVjJEBp3CoLWjAlVvTnwtJRek6YqFBHHY)) as well as the ideas that we shortlisted from these and further elaborated on ([Google Doc](https://docs.google.com/document/d/1HgQeT42FusOr-Ey30zY6k5rxHh36mtnjlis_n5aHfZs/edit?usp=sharing)).

The TA liked all ideas and raised no particular red-flags. However, they provided the following guidelines for the selected idea:

- Narrow down scope of the DSL (OS focused)
- Be careful of OS-specific features
- Ensure entire group is familiar and comfortable with the skills required for the project

## Follow up tasks for the team:

- Investigate File system APIs in Java for move, add, copy, removal, file metadata
- Decide on user experience
- Daemon - In multitasking computer operating systems, a daemon is a computer program that runs as a background process, rather than being under the direct control of an interactive user.
