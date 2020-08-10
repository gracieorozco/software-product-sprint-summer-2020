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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> result = new ArrayList<TimeRange>();
    ArrayList<TimeRange> timeRanges = new ArrayList<TimeRange>();
    ArrayList<Integer> startTimes = new ArrayList<Integer>();
    ArrayList<Integer> endTimes = new ArrayList<Integer>();

    // If the duration is longer than the entire day, return an empty collection. 
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return result;
    }
    
    // If there are no events or attendees in the request, return the whole day. 
    if (events.isEmpty() || request.getAttendees().isEmpty()) {
        timeRanges.add(TimeRange.WHOLE_DAY);
        result.add(TimeRange.WHOLE_DAY);
        return result;
    }

    // Loop through the list of events to add the start and end times for an event of the requested attendees. 
    for (Event event : events) {
        for (String eventPerson : event.getAttendees()) {
            if (request.getAttendees().contains(eventPerson)) {
                startTimes.add(event.getWhen().start());
                endTimes.add(event.getWhen().end());
            } 
        }
    }

    // Check if there are any start and end times to generate meeting times, otherwise, return the whole day.
    if (startTimes.size() > 0 && endTimes.size() > 0) {
        // Sort the start and end times to simplify further processing. 
        Collections.sort(startTimes);
        Collections.sort(endTimes);

        // If the first start time does not begin with 0, include a time starting from 0 to the first start time. 
        if (startTimes.get(0) != 0) {
            timeRanges.add(TimeRange.fromStartEnd(0, startTimes.get(0), false));
            result.add(TimeRange.fromStartEnd(0, startTimes.get(0), false));
        }

        // If the last end time does not end with 1440, include a time ending from 1440 with the last end time. 
        if (endTimes.get(endTimes.size()-1) != TimeRange.WHOLE_DAY.duration()) {
            timeRanges.add(TimeRange.fromStartEnd(endTimes.get(endTimes.size()-1), TimeRange.WHOLE_DAY.duration(), false));
            result.add(TimeRange.fromStartEnd(endTimes.get(endTimes.size()-1), TimeRange.WHOLE_DAY.duration(), false));
        }
    } else {
        result.add(TimeRange.WHOLE_DAY);
        return result;
    }

    // Loop through the end times to combine various availability times with the start times.
    for (int i = 0; i < endTimes.size()-1; i++) {
        if (endTimes.get(i) < startTimes.get(i+1)) {
            timeRanges.add(TimeRange.fromStartEnd(endTimes.get(i), startTimes.get(i+1), false));
            result.add(TimeRange.fromStartEnd(endTimes.get(i), startTimes.get(i+1), false));
        }
    }

    // Loops through the time ranges and removes overlapping times. 
    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);
    Collections.sort(result, TimeRange.ORDER_BY_START);
    for (int i = 0; i < timeRanges.size()-1; i++) { 
        TimeRange currentRange = timeRanges.get(i);
        TimeRange nextRange = timeRanges.get(i+1);
        if (nextRange.overlaps(currentRange)) {
            if (currentRange.contains(nextRange)) {
                result.remove(currentRange);
            } else {
                result.remove(nextRange);
            }
        }
        if (nextRange.start() == nextRange.end()) {
            result.remove(nextRange);
        }
        if (currentRange.start() == currentRange.end()) {
            result.remove(currentRange.duration());
        }
    }
    // Loops through the time ranges once more to remove any ranges that are less than the requested duration. 
    for (int i = 0; i < timeRanges.size(); i++) {
        TimeRange currentRange = timeRanges.get(i);
        if (currentRange.duration() < request.getDuration()) {
            result.remove(currentRange);
        }
    }

    return result;
  }
}
