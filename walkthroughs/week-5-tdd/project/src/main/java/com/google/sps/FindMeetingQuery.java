// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      if (request.getAttendees().size() == 0)
          return performQuery(events, request, true);
      Collection<TimeRange> freeList = performQuery(events, request, true);
      if (freeList.size() == 0)
          return performQuery(events, request, false);
      return freeList;
  }
  
  public Collection<TimeRange> performQuery(Collection<Event> events, MeetingRequest request, boolean considerOptionalAttendees) {
    LinkedList<TimeRange> freeList = new LinkedList<TimeRange>();
    freeList.add(TimeRange.WHOLE_DAY);

    // Carve freeList down into series of non-conflicting chunks.
    for (Event event : events) {
        if (( !considerOptionalAttendees
              || Collections.disjoint(event.getAttendees(), request.getOptionalAttendees()))
        && Collections.disjoint(event.getAttendees(), request.getAttendees()))
            continue;

        int eventStart = event.getWhen().start();
        int eventEnd = event.getWhen().end();
        ListIterator<TimeRange> iter = freeList.listIterator(0);

        while (iter.hasNext()) {
            TimeRange freeBlock = iter.next();
            if (eventEnd <= freeBlock.start())
                break;
            else if (eventStart < freeBlock.end())
                trim(iter, freeBlock, eventStart, eventEnd);
        }
    }

    // Trim off freeBlocks of insufficient size.
    long requiredDuration = request.getDuration();
    ListIterator<TimeRange> iter = freeList.listIterator(0);
    while (iter.hasNext()) {
        TimeRange freeBlock = iter.next();
        if ((long) freeBlock.duration() < requiredDuration)
            iter.remove();
    }

    return(freeList);
  }

  private void trim(ListIterator iter, TimeRange freeBlock, int eventStart, int eventEnd) {
      if (eventStart <= freeBlock.start() && eventEnd >= freeBlock.end())   // Event fully contains free-block
          iter.remove();
      else if (eventStart <= freeBlock.start()) {   // Event overlaps with beginning of free-block
          TimeRange newBlock = TimeRange.fromStartEnd(eventEnd, freeBlock.end(), false);
          iter.remove();
          iter.add(newBlock);          
      }
      else if (eventEnd >= freeBlock.end()) {   // Event overlaps with end of free-block
          TimeRange newBlock = TimeRange.fromStartEnd(freeBlock.start(), eventStart, false);
          iter.remove();
          iter.add(newBlock); 
      }
      else {    // Free-block fully contains event
          TimeRange beforeBlock = TimeRange.fromStartEnd(freeBlock.start(), eventStart, false);
          TimeRange afterBlock = TimeRange.fromStartEnd(eventEnd, freeBlock.end(), false);
          iter.remove();
          iter.add(beforeBlock);
          iter.add(afterBlock);
      }
  }
}
