# photo-notes — specifications by example
# Scenario tags @w<N> map items to delivery waves (see WAVES.md / BACKLOG.md).

Feature: photo-notes

  @w1 @regression
  Scenario: Save is idempotent
    Given a photo with 2 persisted notes opened in the edit screen
    When the user taps confirm twice (or confirms after re-opening)
    Then the Note table still contains exactly 2 rows for that fileName

  @w1 @edge-case
  Scenario: Notes load before interaction
    Given a photo with persisted notes
    When the edit screen opens
    Then markers appear only after the DB read completes, and a tap during
      loading cannot corrupt the notes list

  @w2 @happy-path
  Scenario: Manage a note
    Given a photo showing note markers
    When the user taps a marker
    Then a dialog shows the note text with Edit, Delete, and Cancel actions

  @w2 @happy-path
  Scenario: Edit persists
    Given an existing note "fix lighting"
    When the user edits it to "fix lighting + crop" and confirms
    Then the marker dialog shows the new text and the DB row is updated

  @w2 @happy-path
  Scenario: Delete persists
    Given a photo with 2 notes
    When the user deletes one via its marker dialog
    Then its marker disappears and the DB row is gone; the other note
      remains

  @w3 @edge-case
  Scenario: Empty note rejected
    Given the edit screen open on a photo
    When the user taps the photo and submits an empty or whitespace-only
      comment
    Then no note or marker is created and the user sees a message
