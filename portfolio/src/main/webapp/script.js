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
 * Adds a random fact to the page.
 */
function addRandomFact() {
  const facts =
      ['I major in computer science.', 'I am learning new langauges.', 'I have traveled to over 30 states.'];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

function fetchComments() {
    fetch("/data").then(request => request.json()).then((textResponse) => {
        for (var i = 0; i < textResponse.length; i++) {
            var paragraph_tag = document.createElement("p");
            paragraph_tag.innerText = textResponse[i]['comment'];
            document.getElementById("comments-container").appendChild(paragraph_tag);
        }
    });
}