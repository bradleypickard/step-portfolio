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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['That\'s rough buddy.', 'My cabbages!', 'Flameo, hotman!', 'Who are you? And what do you want?', 'Yip yip!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const container = document.getElementById('greeting-container');
  container.innerText = greeting;
}

/**
 * Selects the contents of the 'About' section.
 */
function setAboutContentTo(section) {
  document.getElementById('background').style.display = "none";
  document.getElementById('interests').style.display = "none";
  document.getElementById('projects').style.display = "none";
  
  document.getElementById(section).style.display = "block";
}

/**
 * Selects the contents of the 'Mood' section.
 */
function setMoodTo(mood) {
  document.getElementById('happy').style.display = "none";
  document.getElementById('sleepy').style.display = "none";
  document.getElementById('focused').style.display = "none";
  document.getElementById('energetic').style.display = "none";
  document.getElementById('goofy').style.display = "none";
  
  document.getElementById(mood).style.display = "block";
}
