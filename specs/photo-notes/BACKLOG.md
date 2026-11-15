# Backlog: photo-notes

Items are USER STORIES in the parseable format below — keep the heading
shape `## US<n> — <story>` and the field lines intact so `wave_plan` can
parse them. Write the story as: "As a <role>, I want <goal>, so that
<value>" — the title after the dash IS the story.

```
## US<n> — As a <role>, I want <goal>, so that <value>
- Scenarios: @w1, @w2
- Depends: US1, US3        (optional; omit for wave-1 items)
- Size: S|M|L
- Notes: <one line>
```

Order stories by user value; dependencies express technical order only.

## US1 — As a user, I want saving my annotated photo to never duplicate notes, so that my annotations stay correct
- Scenarios: @w1
- Size: M
- Notes: deleteByFileName query + transactional rewrite of persistData; fix notes-list threading

## US2 — As a user, I want to view, edit, and delete notes I've placed, so that I can fix mistakes
- Scenarios: @w2
- Depends: US1
- Size: M
- Notes: marker ImageViews tagged by UUID + click dispatch + edit/delete dialogs + DAO update/delete

## US3 — As a user, I want empty comments rejected, so that stray taps don't create junk notes
- Scenarios: @w3
- Depends: US2
- Size: S
- Notes: trim+nonempty check in addNotesPosition with Toast/message
