/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.io.erasurecode.rawcoder;

import java.nio.ByteBuffer;

/**
 * A raw decoder in XOR code scheme in pure Java, adapted from HDFS-RAID.
 */
public class XorRawDecoder extends AbstractRawErasureDecoder {

  @Override
  protected void doDecode(ByteBuffer[] inputs, int[] erasedIndexes,
                          ByteBuffer[] outputs) {
    assert(erasedIndexes.length == outputs.length);
    assert(erasedIndexes.length <= 1);

    int bufSize = inputs[0].remaining();
    int erasedIdx = erasedIndexes[0];

    // Set the output to zeros.
    for (int j = 0; j < bufSize; j++) {
      outputs[0].put(j, (byte) 0);
    }

    // Process the inputs.
    for (int i = 0; i < inputs.length; i++) {
      // Skip the erased location.
      if (i == erasedIdx) {
        continue;
      }

      for (int j = 0; j < bufSize; j++) {
        outputs[0].put(j, (byte) (outputs[0].get(j) ^ inputs[i].get(j)));
      }
    }
  }

  @Override
  protected void doDecode(byte[][] inputs, int[] erasedIndexes,
                          byte[][] outputs) {
    assert(erasedIndexes.length == outputs.length);
    assert(erasedIndexes.length <= 1);

    int bufSize = inputs[0].length;
    int erasedIdx = erasedIndexes[0];

    // Set the output to zeros.
    for (int j = 0; j < bufSize; j++) {
      outputs[0][j] = 0;
    }

    // Process the inputs.
    for (int i = 0; i < inputs.length; i++) {
      // Skip the erased location.
      if (i == erasedIdx) {
        continue;
      }

      for (int j = 0; j < bufSize; j++) {
        outputs[0][j] ^= inputs[i][j];
      }
    }
  }

}
