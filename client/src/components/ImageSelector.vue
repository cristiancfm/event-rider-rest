<template>
  <div>
    <div class="mb-3">
      <label for="image-input" class="form-label">
        Images (max. {{ this.maxFiles }} files) (max. file size:
        {{ this.bytesToMB(this.maxFileSize) }} MB) (optional)
      </label>
      <input
        id="image-input"
        class="form-control"
        type="file"
        ref="fileInput"
        @change="handleImageUpload"
        accept="image/*"
        multiple
      />
    </div>

    <ul class="list-group">
      <li
        class="list-group-item d-flex justify-content-between align-items-center p-2"
        v-for="(image, index) in imagesList"
        :key="index"
      >
        <div class="col">
          <div class="position-relative">
            <img
              :src="image.url"
              :alt="image.name"
              class="d-block w-100"
              style="
                object-fit: cover;
                object-position: center;
                aspect-ratio: 3/2;
              "
              @error="setPlaceholder"
            />
            <button
              class="btn btn-secondary position-absolute top-0 end-0 m-2"
              @click.prevent="deleteImage(index)"
            >
              <i class="bi bi-trash-fill" />
            </button>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <br />
</template>

<script>
export default {
  name: "ImageSelector",
  props: {
    maxFiles: {
      type: Number,
      default: 10,
    },
    maxFileSize: {
      type: Number,
      default: 5 * 1024 * 1024, // 5 MB
    },
    serverImages: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      // the 3 lists below contain a list of elements with the following:
      // {url (generated url to show the image), name (name of the image), file (image file)}
      imagesList: [],
      imagesToUpload: [],
      imagesToDelete: [],
    };
  },
  methods: {
    bytesToMB(bytes) {
      return bytes / (1024 * 1024);
    },
    setPlaceholder(event) {
      event.target.src = "/error-placeholder.png";
    },
    handleImageUpload(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (!file.type.startsWith("image/")) {
          alert("File is not an image");
          return;
        }
        if (
          this.imagesList.length + this.imagesToUpload.length >=
          this.maxFiles
        ) {
          alert(`Maximum number of files reached (${this.maxFiles} files)`);
          return;
        }
        if (file.size > this.maxFileSize) {
          alert(
            `File size exceeds the maximum limit (${this.bytesToMB(
              this.maxFileSize
            )} MB)`
          );
          return;
        }
        const reader = new FileReader();
        reader.onload = () => {
          const image = {
            url: reader.result,
            name: file.name,
            file: file,
          };
          this.imagesToUpload.push(image);
          this.imagesList.push(image);
        };
        reader.readAsDataURL(file);
      }
    },
    deleteImage(index) {
      if (index >= 0 && index < this.imagesList.length) {
        const deletedImage = this.imagesList.splice(index, 1)[0];
        if (this.imagesToUpload.includes(deletedImage)) {
          this.imagesToUpload.splice(
            this.imagesToUpload.indexOf(deletedImage),
            1
          );
        } else {
          this.imagesToDelete.push(deletedImage);
        }
      }
    },
  },
};
</script>
