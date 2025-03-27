import React, { useState } from "react";
import axios from "axios";

const RepairForm = () => {
  const [repairData, setRepairData] = useState({
    name: "",
    phoneNumber: "",
    email: "",
    priority: "",
    area: "",
    description: "",
    assetName: "",
    assetCode: "",
    scheduled: false,
  });
  const [imageFile, setImageFile] = useState(null);

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setRepairData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle image file selection
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
  };

  // Submit form data to the backend
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    // Append the repair data (as a JSON string) to the FormData
    formData.append("repair", JSON.stringify(repairData));

    // Append the image file to the FormData
    if (imageFile) {
      formData.append("image", imageFile);
    }

    try {
      const response = await axios.post("http://localhost:8080/api/repairs", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Repair created successfully:", response.data);
    } catch (error) {
      console.error("There was an error submitting the form:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Name:</label>
        <input
          type="text"
          name="name"
          value={repairData.name}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Phone Number:</label>
        <input
          type="text"
          name="phoneNumber"
          value={repairData.phoneNumber}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Email:</label>
        <input
          type="email"
          name="email"
          value={repairData.email}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Priority:</label>
        <input
          type="text"
          name="priority"
          value={repairData.priority}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Area:</label>
        <input
          type="text"
          name="area"
          value={repairData.area}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Description:</label>
        <textarea
          name="description"
          value={repairData.description}
          onChange={handleChange}
          required
        ></textarea>
      </div>
      <div>
        <label>Asset Name:</label>
        <input
          type="text"
          name="assetName"
          value={repairData.assetName}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Asset Code:</label>
        <input
          type="text"
          name="assetCode"
          value={repairData.assetCode}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label>Scheduled:</label>
        <input
          type="checkbox"
          name="scheduled"
          checked={repairData.scheduled}
          onChange={(e) =>
            setRepairData((prevData) => ({
              ...prevData,
              scheduled: e.target.checked,
            }))
          }
        />
      </div>
      <div>
        <label>Image:</label>
        <input type="file" onChange={handleImageChange} />
      </div>
      <button type="submit">Submit Repair</button>
    </form>
  );
};

export default RepairForm;
